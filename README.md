# LSystemGenerator

A JavaFX tool for building L-systems and looking at what they grow into. Give it an axiom and a rule set, it expands them, and a turtle walks the result into a figure that can be exported as OBJ and opened in Blender.

```
LSystem(axiom=F, rules={F=FF+[+F-F-F]-[-F+F+F]}, iterations=6, angle=90)
```

<img width="300" alt="An L-system generated in the app" src="https://github.com/Slaymish/LSystemGenerator/assets/21288505/b64e5cae-96dc-4f1d-8349-a6f91cb40059">
<img width="300" alt="The same L-system rendered in Blender" src="https://github.com/Slaymish/LSystemGenerator/assets/21288505/e3bf0f78-daff-4851-9b88-57dd885f757e">

## What it does

- Expand an L-system from an axiom, a rule set, an angle and an iteration count.
- Save and load systems, so a good one can be kept and changed later.
- Export the turtle geometry as OBJ for Blender.

## Running it

```bash
./mvnw javafx:run
```

Needs JDK 17 or newer. The Maven wrapper is in the repo, so there is nothing to install first.

## State

Written in May 2023. Building a full 3D mesh from the triangles is still unfinished, which is why the export is a flat figure that usually gets extruded in Blender. The rest of the old TODO list is done.

MIT licensed.
