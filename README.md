# Project Ragnarok


## TO-DO

- **Projekt ausslimmen.**
  - Pakete rauslöschen
  - stupiden LevelCreator schreiben (und alles andere Level-relatete rauslöschen)
- **JavaDocs schreiben**
- **Folien**
  - Architektur
  - Wichtigste Erklärungen (GameElement)
  - Planung - Entwurf - Implementierung
- **Aufgabenzettel**
  - Gegner *(Schablonenmethode, Strategie/State, Kompositum/Fabrikmethode)*
    - z.B. hüpfend
    - z.B. aufsplittend
    - mehrere Leben -> mehrere Verhaltensmuster
  - Items *(Schablonenmethode, State)*
    - z.B. Score-Item
    - z.B. Leben
    - temporäre PowerUps
      - z.B. schneller
      - z.B. unsterblich
  - LevelCreator *(Fliegengewicht)*
  - Angriff *(State, Fabrikmethode)*
    - z.B. Nahkampf
    - z.B. Fernkampf
  - Background *(Kompositium/Mediator)*
    - Mehrere Layer, zufällig zusammengesetzt
  - Spezialblöcke *(Schablonenmethode, State, Fabrikmethode)*
    - z.B. bewegliche Plattformen
    - z.B. "alternierende" Blöcke
    - z.B. Stacheln / Lava
    - z.B. explodierende Plattformen (per Particles)
