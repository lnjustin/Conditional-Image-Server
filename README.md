# Conditional-Image-Server
Serve different JPG, PNG, or SVG images from your Hubitat Elevation hub according to rules in Rule Machine

Example use cases:
## Dynamic Dashboard Background ##
Set different background images for your smart home dashboard based upon device state, weather, holidays, location mode, or any other condition. For instance, set your dashboard background to a rainy landscape or an umbrella when it's raining, to a sunny meadow when it's clear skies, a Christmas tree on Christmas, a birthday cake on your birthday, etc.
## Custom Icon Tile ##
Configure custom icons that change with device state, location mode, etc.

## Supported Image Types ##
1. JPG
2. SVG
3. PNG

**Install Instructions**
1. Install Conditional Image Server app and Dynamic Image URL Device driver, either manually or via Hubitat Package Manager
2. Enable OAth if installed manually
3. Configure different URLs with different images
4. Use the included custom device or create a Global Variable Connector in Rule Machine
5. Set up rules in Rule Machine that set the value of the custom device or a Global Variable Connector to different ones of the configured URLs under different conditions
6. Point your dashboard background, image tile, etc. to the local URL in the Conditional Image Server app. Your different images will dynamically load from this single URL under the different conditions you established in Rule Machine.

**Local Only**
Note this only works locally. Hubitat does not currently support rendering of images at a cloud endpoint for remote access.

Enjoy!
