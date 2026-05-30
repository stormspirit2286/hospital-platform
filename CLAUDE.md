# CLAUDE.md — Working Agreement

> **MUST READ before every action in this repository.**
> These rules are non-negotiable. Violating them wastes the user's time.

---

## 1. Code Style — Hard Rules

### 1.1. NEVER write Vietnamese comments in code
- All code comments **MUST be in English** (or omitted entirely).
- This includes: inline comments, Javadoc, TODO, FIXME, block comments, log messages inside code.
- Reason: code is read by tools, IDE, and future contributors. Mixed-language comments pollute the codebase.

### 1.2. NEVER add explanatory comments to code unless explicitly asked
- Do **NOT** write comments like `// xử lý logic` / `// handle logic` / `// validate input`.
- Do **NOT** add Javadoc unless the user requests it.
- The code itself must be self-documenting through clear naming.
- Comments are allowed ONLY for:
  - Non-obvious business rules with a real reason
  - Workarounds with context (e.g. `// Workaround for Hibernate bug HHH-12345`)
  - Public API contracts when the user explicitly asks
- When in doubt: **do not write the comment**.

### 1.3. NEVER add decorative section headers in code
- Do NOT write `// ===== Section Name =====` or `// --- Helpers ---` in production code.
- Group related methods by position only. Let the structure speak.

### 1.4. NEVER use emoji in code or commit messages
- Emoji belongs in chat replies, not in files.

---

## 2. Chat / Reply Style

### 2.1. Vietnamese for chat is fine
- User communicates in Vietnamese. Reply in Vietnamese when explaining.
- This is the ONLY place Vietnamese is allowed.

### 2.2. Be concise
- The user is a developer. Skip filler. Skip restating the question.
- Prefer tables / short bullet lists over long prose.

### 2.3. When the user says "explain, don't code" — respect it
- Do not produce code blocks when the user explicitly asks for explanation only.
- The user is learning by writing code themselves.

### 2.4. Admit mistakes immediately
- If the user challenges a previous answer and they are right, acknowledge it directly.
- Do not double down. Do not soften with "well actually".

---

## 3. Architecture & Design Defaults

### 3.1. DTO vs Entity
- Request DTOs must NEVER expose server-controlled fields (`id`, `userId`, `status`, `createdAt`, `role`, etc.).
- Mass assignment must be prevented at the DTO layer, not the service layer.

### 3.2. MapStruct
- Always set `componentModel = "spring"`.
- Always set `unmappedTargetPolicy = ReportingPolicy.ERROR`.
- Bidirectional JPA relationships are set in the service layer, not the mapper.
- Method overload by input type (`toResponse(X)`, `toResponse(Y)`) is the default naming.

### 3.3. JPA
- All `@Enumerated` must be `EnumType.STRING`. Never `ORDINAL`.
- All associations use `FetchType.LAZY` by default.
- Use `JOIN FETCH` or `@EntityGraph` to solve N+1 when listing.
- Aggregate Root pattern: one repository per aggregate root. Do not create a repository for every entity.

### 3.4. Liquibase
- Never modify a committed changelog file. Always create a new one.
- One logical change per changeset. Use `IF EXISTS` / `IF NOT EXISTS` for idempotency.
- Master changelog uses `includeAll` — new SQL files are auto-picked up.

### 3.5. Response Wrapper
- All HTTP responses use `ApiResponse<T>` (see `dto/response/common/`).
- Errors are emitted from `GlobalExceptionHandler`, never from controllers directly.
- Response codes are centralized in `ResponseCode` enum.

---

## 4. Project Domain Notes

- This is a **Vietnamese hospital platform**.
- "Insurance" here means **BHYT (state health insurance) only** — not life/voluntary insurance.
- BHYT card number format: `^[A-Z]{2}[0-9]{13}$` (15 chars total).
- Patient ↔ Insurance is **1-1** (UNIQUE constraint on `patient_id`).
- Patient ↔ EmergencyContact is **1-N**, capped at **2** by service-layer validation.

---

## 5. Workflow

### 5.1. Default action order when adding a feature
1. DTO Request
2. DTO Response
3. Exception classes
4. Mapper (MapStruct)
5. Service interface
6. Service impl
7. GlobalExceptionHandler entries (if new exception types)
8. Controller (thin, returns `ApiResponse`)

### 5.2. After editing
- Run `./mvnw -q compile` to verify (don't rely on IDE warnings).
- Do not call `get_errors` on unrelated files.

### 5.3. Progress tracking
- Update `PROJECT_PROGRESS.md` only when the user asks.

---

## 6. Anti-patterns to NEVER produce

- `// TODO: implement later` without a real plan
- Mass-assigning request body straight into entity
- Catching exceptions just to rethrow them
- Adding `@Transactional` to read-only methods without `readOnly = true`
- Inner DTO classes inside a parent DTO (always separate files)
- `System.out.println` — use a logger
- Reactive Redis in a blocking WebMVC service
- Cyclical references in DTOs (parent → child → parent)

---

## 7. Reminder

If you (Claude) catch yourself about to:
- Write a Vietnamese comment → stop, rewrite in English or remove it.
- Add a comment "explaining what the code does" → stop, delete it.
- Add `===== Section =====` headers → stop, delete them.
- Apologize three times in a row → stop, just fix the issue.

The user has explicitly stated these rules. Repeating the mistake is a failure of attention, not a misunderstanding.

