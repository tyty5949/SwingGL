# SwingGL
A library that uses the Java OpenGL port LWJGL to render UI elements and other 2D sprites much like Java Swing. It features customizable elements housed in a singular sprite-sheet for maximum performance. It also included a variable multi-threaded game loop that can be altered to run differently on the fly. The goal is to create a versatile engine that will allow quick creation of complex yet efficient UI's and 2D games.

#### Current UI Elements
  - Window Frames
  - Window Panels
  - Buttons

#### Planned UI Elements
  - Check Boxes
  - Radio Buttons
  - Combo Boxes
  - Dialogues
  - Text Boxes
  - Text Fields
  - Scroll Bars
  - Scrollable Lists
  
#### Backend Features
  - Variable Multi-Threaded Game Loop
  - Textures
  - TrueTypeFonts

#### Planned Backend Features
  - Dubugging/Console Interface
  - Rendering Batches
  - Entities
  - Sprites
  - Sprite-Sheets

### TODO
  - GLFrame needs events rather than actually calling the OpenGL functions from its methods. Ex. frame.setBackground should be able to be called anywhere, not just in the render or initialize method where there is OpenGL context. It should trigger and event and the next time render runs it should set the background.
  - Figure out why the TrueTypeFont class does not generate accurate widths and heights
