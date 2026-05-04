# Feature Chat Module - AGENTS.md

`feature/chat` handles real-time messaging.

## Keep In Mind

- WebSocket messaging uses the **Krossbow** STOMP client library; preserve its session lifecycle and subscription handling.
- Use Flow/Result patterns already established in the module.
- Keep message state, room state, and connection handling separate.

## Read First

- Root `AGENTS.md`
- `core/AGENTS.md`
