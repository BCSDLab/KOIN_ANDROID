# Feature Timetable Module - AGENTS.md

`feature/timetable` handles class schedules and timetable workflows.

## Keep In Mind

- Use the existing Compose and Orbit patterns for screens and ViewModels.
- Keep lecture, frame, and schedule logic within this module.
- Prefer `Flow<T>` for streaming timetable data where it already exists.

## Read First

- Root `AGENTS.md`
- `core/AGENTS.md`
