# Automated Gherkin & BDD Test Scenario Generator

A developer tool that reads software requirements written as **EARS** sentences or **user stories** and translates them into **Gherkin** feature files (Given / When / Then) for Behavior-Driven Development. Free-text requirements can optionally be rewritten as EARS sentences by an external LLM service.

```text
Input (login.txt)
  REQ-3: While the user is on the login page, when the user enters a wrong password,
         the system shall display an error message.

Output (user_login.feature)
  Feature: User login
    @REQ-3 @positive
    Scenario: The user enters a wrong password
      Given the user is on the login page
      When the user enters a wrong password
      Then the system displays an error message
```

CSE 443 Object-Oriented Analysis and Design term project, Gebze Technical University, Fall 2026.

## Team CURATORES

| Name | Student ID |
| --- | --- |
| Emre Kaan Şahin | 220104004047 |
| Halit Batuhan Aslan | 220104004003 |
| Ömer Faruk Gedikli | 220104004103 |

## Deliverables

| # | Deliverable | Location | Status |
| --- | --- | --- | --- |
| 1 | Requirements Analysis Document (RAD) | [`docs/deliverable1-rad/`](docs/deliverable1-rad/) | Due 30 Oct 2026, 23:59 |
| 2 | System Design Document (SDD) | `docs/` | Planned |
| 3 | Software Test Document (STD) | `docs/` | Planned |
| 4 | Implementation and presentation | `src/`, `docs/` | Planned |

## Repository structure

```text
.
├── docs/
│   ├── deliverable1-rad/   RAD (.docx and .pdf)
│   └── diagrams/           PlantUML sources and images of all RAD figures
├── samples/                Example requirement files used in the RAD scenarios
│   └── expected/           Expected generator output for the samples
├── src/main/java/          Source code (Java 17)
├── src/test/java/          Unit tests (JUnit 5)
└── pom.xml                 Maven build
```

The source packages follow the analysis model of the RAD:

| Package | Responsibility |
| --- | --- |
| `cli` | Command-line interface |
| `gui` | Optional graphical interface |
| `input` | Reading, format detection and validation of requirements |
| `model` | Requirements and the Gherkin model (Feature, Scenario, Step) |
| `generation` | Translation of requirements into scenarios |
| `output` | Gherkin, Markdown and JSON output |
| `llm` | Gateway to the external LLM service |

## Build

Requirements: Java 17 or later and Maven 3.9 or later.

```bash
mvn verify                      # compile and run the tests
java -jar target/gherkingen.jar # run the tool (not implemented yet)
```

## Sample inputs

The files in `samples/` are the inputs of the scenarios in Section 3.4.1 of the RAD:

| File | Format | Used in |
| --- | --- | --- |
| `login.txt` | EARS | generateLoginScenarios, Mock-up 2, Figure 13 |
| `payment.txt` | EARS with two invalid lines (4 and 9) | fixInvalidRequirements, Mock-up 3 |
| `cart.txt` | User stories with acceptance criteria | userStoriesToMarkdown, Mock-up 4 |
| `security.txt` | EARS | negativeAndBoundaryScenarios, Section 3.1, Mock-up 5 |
| `notes.txt` | Free text | freeTextWithLlm, Mock-up 6 |

## Working on the repository

- `main` always holds a version that can be handed in.
- Work on a branch (`docs/...`, `feature/...`, `fix/...`) and merge it with a pull request reviewed by another team member.
- Every deliverable is tagged: `v0.1-rad`, `v0.2-sdd`, `v0.3-std`, `v1.0-final`.
- Never commit API keys; the LLM key is read from an environment variable.
