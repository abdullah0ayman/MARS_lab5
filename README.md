# MARS_lab5

This repository contains the complete organized MARS-OPS Programming II project.

The project is intentionally simple and uses **no Java enums**. Mission types, mission states, emergency types, facility types, environmental conditions, health statuses, and resource names are represented with strings.

## Project location

Open the complete project here:

```text
MARS-OPS-Organized-Project/
```

## Compile and run

```bash
cd MARS-OPS-Organized-Project
rm -rf out
mkdir out
javac -Xlint:all -d out $(find src -name '*.java')
java -cp out Main
```

See `MARS-OPS-Organized-Project/docs/PROJECT-STRUCTURE.md` for the responsibility of every folder and class.
