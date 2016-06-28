#Project Ragnarok


## TO-DO

### Explanation
[ ] Means currently not in work  
[-] Means currently being worked at  
[x] Means done, will be removed later  
[?] Means done, but not sure. Remove if someone is sure  

### Features
- [ ] use Bitbucket Issues for Features and Bugs?
- [-] Menu (Start Game, Settings, About)
- [-] divide Game in Scenes (MenuScene, InfinitPlayScene, LevelOfTheDayScene)
- [ ] Show score/highscore dependent death-messages (Restart option/countdown)
- [-] Create Background and parallax it (2 or 3 layers)

#### Visual
- [ ] Player loose glasses on hit (show tiny eys while invincible)

### Refactoring
- [ ] Put collision detection in separate class
- [ ] Don't give Entity full access to GameModel --> Interface
- [x] Create Boss super class
- [ ] Create ParticleSpawnerFactory

### Nice to have
- [ ] Player pickup: clear Screen (kill enemys)
- [ ] Enemy pickups (makes the enemy tougher)

### Bugs
- [ ] Fix z-Index rendering order
- [ ] SlurpDurps sometimes seem to not affect the Player
- [ ] RektKillers sometimes get stuck in levelBorders (also: what to do with left border?)
- [ ] GameConf.logicDelta affects jump height  
- [ ] Concurrency exception in control because pressed keys or released keys changes while observers are updating (synchronized copying causes laggy jumping)

## Archive

### Features
- [x] Show highscore below score and save highscore
- [x] Allow shorter Jumps
- [x] Bosses

### Nice to have

### Bugs
- [x] Fix collision detection direction
- [x] Secret Exit Bug
- [x] Lazy Initialization Bug in Rocket explosion & spark particles
- [x] Slurp colliding with InanimateFloor cause Particle madness
- [x] org.eclipse.swt.SWTError: No more handles when playing for a long time