#Project Ragnarok


## TO-DO

### Explanation
[ ] Means currently not in work  
[-] Means currently being worked at  
[x] Means done, will be removed later  
[?] Means done, but not sure. Remove if someone is sure  

### Features
- [ ] Menu (Start Game, Settings, About)
- [ ] Show score/highscore dependent death-messages (Restart option/countdown)
- [ ] Create Background and parallax it (2 or 3 layers)
- [ ] Bosses

### Refactoring
- [ ] Put collision detection in separete class
- [ ] Don't give Entity full access to GameModel --> Interface

### Nice to have
- [ ] Player pickup: clear Screen (kill enemys)
- [ ] Enemy pickups (makes the enemy tougher)

### Bugs
- [ ] Fix z-Index rendering order
- [ ] SlurpDurps sometimes seem to not affect the Player
- [ ] RektKillers sometimes get stuck in levelBorders (also: what to do with left border?)
- [ ] GameConf.logicDelta affects jump height  

## Archive

### Features
- [x] Show highscore below score and save highscore
- [x] Allow shorter Jumps

### Nice to have

### Bugs
- [x] Fix collision detection direction
- [x] Secret Exit Bug
- [x] Lazy Initialization Bug in Rocket explosion & spark particles
- [x] Slurp colliding with InanimateFloor cause Particle madness
- [x] org.eclipse.swt.SWTError: No more handles when playing for a long time
