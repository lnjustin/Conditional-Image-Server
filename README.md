# Conditional-Image-Server
Serve different JPG images from your Hubitat Elevation hub according to rules in Rule Machine

Example use case: Set different background images for your smart home dashboard based upon device state, weather, holidays, location mode, or any other condition.

**Install Instructions**
1. Install Conditional Image Server app
2. Configure different URLs with different JPG images
3. Create a Global Variable Connector in Rule Machine
4. Set up rules in Rule Machine that set the value of the created Global Variable Connector to different ones of the configured URLs under different conditions
5. Open the COnditional Image Server app
6. Select the created Global Variable Connector
7. Note the local URL in the Conditional Image Server app. This is the URL you will use. Your different JPG images will dynamically load from this single URL under the different conditions you established in RUle Machine.
8. Press Done

**Local Only**
Note this only works locally. Hubitat does not currently support rendering of JPGs at a cloud endpoint for remote access.

Enjoy!
