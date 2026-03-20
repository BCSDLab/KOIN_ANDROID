#!/usr/bin/env bash

set -euo pipefail

if [[ "$#" -ne 3 ]]; then
  echo "Usage: $0 <suite_path> <suite_name> <app_id>" >&2
  exit 1
fi

suite_path="$1"
suite_name="$2"
app_id="$3"

mkdir -p recordings debug-output .maestro/.generated

wrapper_flow=".maestro/.generated/${suite_name}_recorded.yaml"
relative_suite_path="$suite_path"
junit_output="recordings/${suite_name}-result.xml"
video_output="recordings/${suite_name}.mp4"
debug_output="debug-output/${suite_name}"

if [[ "$suite_path" == .maestro/* ]]; then
  relative_suite_path="../${suite_path#.maestro/}"
fi

cat > "$wrapper_flow" <<EOF
appId: "\${APP_ID}"
env:
  APP_ID: "${app_id}"
onFlowStart:
  - startRecording:
      path: "../../recordings/${suite_name}"
onFlowComplete:
  - stopRecording
---
- runFlow: ${relative_suite_path}
EOF

echo "Running Maestro suite: ${suite_name}"

start_time="$(date +%s)"
test_exit_code=0
record_args=(
  --local
  --debug-output "$debug_output"
  -e "APP_ID=$app_id"
  "$wrapper_flow"
  "$video_output"
)

if ! maestro record "${record_args[@]}"; then
  test_exit_code=1
fi
end_time="$(date +%s)"
duration="$((end_time - start_time))"

if [[ "$test_exit_code" -eq 0 ]]; then
  failure_count="0"
  testcase_status='status="SUCCESS"'
  failure_block=""
else
  failure_count="1"
  testcase_status='status="FAILED"'
  failure_block='<failure message="Maestro flow failed"/>'
fi

cat > "$junit_output" <<EOF
<?xml version='1.0' encoding='UTF-8'?>
<testsuites>
  <testsuite name="Test Suite" tests="1" failures="${failure_count}" time="${duration}.0">
    <testcase id="${suite_name}" name="${suite_name}" classname="${suite_name}" time="${duration}.0" ${testcase_status}>${failure_block}</testcase>
  </testsuite>
</testsuites>
EOF

exit "$test_exit_code"
