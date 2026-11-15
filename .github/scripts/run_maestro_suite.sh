#!/usr/bin/env bash
set -euo pipefail

if [[ $# -lt 2 || $# -gt 3 || ! "$1" =~ ^[a-z][a-z0-9_]*$ ]]; then
  echo "Usage: $0 <feature> <emulator-serial> [stage-apk]" >&2
  exit 2
fi

feature="$1"
serial="$2"
flow=".maestro/${feature}.yaml"
output="build/maestro/${feature}/${serial}"
remote_base="/data/local/tmp/koin-maestro-${feature}-$$"
recorder_pid=""

[[ -f "$flow" ]] || { echo "Missing flow: $flow" >&2; exit 2; }
[[ "$serial" =~ ^emulator-[0-9]+$ ]] || { echo "An explicit emulator serial is required" >&2; exit 2; }
mkdir -p "$output"

# Poll the actual boot property, never a fixed startup delay.
timeout 180s bash -c '
  set -euo pipefail
  adb -s "$1" wait-for-device
  until [[ "$(adb -s "$1" shell getprop sys.boot_completed | tr -d "\r")" == 1 ]]; do :; done
' _ "$serial"
printf '%s sys.boot_completed=1\n' "$serial"

if [[ $# == 3 ]]; then
  adb -s "$serial" install -r -t "$3"
fi
adb -s "$serial" shell pm path in.koreatech.koin.dev | grep -q '^package:'

finish() {
  local status=$?
  trap - EXIT INT TERM
  if [[ -n "$recorder_pid" ]]; then
    # SIGINT finalizes the MP4; wait for the recorder instead of sleeping.
    adb -s "$serial" shell "if [ -f '${remote_base}.pid' ]; then kill -2 \"\$(cat '${remote_base}.pid')\"; fi" || status=1
    wait "$recorder_pid" || status=1
    adb -s "$serial" pull "${remote_base}.mp4" "$output/screen.mp4" || status=1
    adb -s "$serial" shell rm -f "${remote_base}.pid" "${remote_base}.mp4" || status=1
    [[ -s "$output/screen.mp4" ]] || status=1
  fi
  exit "$status"
}
trap finish EXIT
trap 'exit 130' INT
trap 'exit 143' TERM

# The test deadline is shorter than screenrecord's API-34 recording limit.
adb -s "$serial" shell "echo \$\$ > '${remote_base}.pid'; exec screenrecord --time-limit 180 --bit-rate 4000000 '${remote_base}.mp4'" > "$output/screenrecord.log" 2>&1 &
recorder_pid=$!
# Wait for the recording file, and fail if the encoder never starts.
timeout 15s bash -c '
  until adb -s "$1" shell test -s "$2"; do
    kill -0 "$3" || exit 1
  done
' _ "$serial" "${remote_base}.mp4" "$recorder_pid"

timeout 150s maestro --udid "$serial" test \
  --format JUNIT \
  --output "$output/result.xml" \
  --debug-output "$output/debug" \
  --test-output-dir "$output" \
  "$flow"
