# Diagrams

PlantUML sources of every figure in the Requirements Analysis Document. Each `.puml` file has a `.png` with the same name, identical to the image used in the document. All diagrams share the style in `skin.iuml`.

| Figure | Source | Content |
| --- | --- | --- |
| 1 | `usecase.puml` | Use case diagram |
| 2 | `domain.puml` | Domain (entity) object model |
| 3 | `enums.puml` | Enumerations of the domain model |
| 4 | `analysis.puml` | Boundary, control and entity objects |
| 5 | `seq_load.puml` | Sequence diagram: Load Requirements File (UC-1, UC-2) |
| 6 | `seq_generate.puml` | Sequence diagram: Generate Scenarios (UC-3, UC-4) |
| 7 | `seq_normalize.puml` | Sequence diagram: Normalize Free-Text Requirement (UC-9) |
| 8 | `seq_report.puml` | Sequence diagram: View Error Report (UC-5) |
| 9 | `seq_export.puml` | Sequence diagram: Export Scenarios (UC-7, UC-8) |
| 10 | `activity.puml` | Activity diagram: generation session |
| 11 | `state_requirement.puml` | State machine: Requirement |
| 12 | `state_session.puml` | State machine: GenerationSession |
| 13 | `gui_mockup.puml` | GUI mock-up: main window |
| 14 | `gui_navigation.puml` | GUI navigation path |
| 15 | `gui_error.puml` | GUI mock-up: error report |
| 16 | `gui_normalize.puml` | GUI mock-up: normalization dialog |
| 17 | `gui_overwrite.puml` | GUI mock-up: overwrite dialog |
| 18 | `gui_result.puml` | GUI mock-up: export result |

To regenerate the images (PlantUML and Graphviz installed):

```bash
cd docs/diagrams
plantuml -tpng *.puml
```
