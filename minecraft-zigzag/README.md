# README

## minecraft_zigzag_1_8_9

* Minecraft 1.8.9
* Sprinting
* No jumping
* Normal, flat surface with no obstacles
* No effects
* Pixel-based mouse movements
* Goal
  * Start at (-0.5,-,0.5),(0,-,0),(0,-)
  * Maximize Z position
  * Do not fall off from the path, which goes on in the following pattern:

```plaintext
X=0
+Z
^#  
|#  
|###
|  #
|  #
|###
|#  
|#  
|###
|  #
|  #
|P## Z=-1
+--->-X
```
