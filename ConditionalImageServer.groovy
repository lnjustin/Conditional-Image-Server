/**
 *  Conditional Image Server
 *
 *  Copyright\u00A9 2020 Justin Leonard
 *
 * Many thanks to @dman2306 help in rendering jpg output
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 * V1.0 - Initial Release
 * V1.1 - Added SVG and PNG support; Added option to use custom device
 * V1.2 - Added GIF support
**/

definition(
        name: "Conditional Image Server",
        namespace: "lnjustin",
        author: "Justin Leonard",
        description: "Conditional Image Server",
        category: "Convenience",
        iconUrl: "",
        iconX2Url: "",
        oauth: [displayName: "Conditional Image Server", displayLink: ""],
    	singleInstance: true
){}

preferences {
    page(name: "settings")
}

def settings(){
    if(!state.accessToken){	
        //enable OAuth in the app settings or this call will fail
        createAccessToken()	
    }
    def localUri = getFullLocalApiServerUrl() + "/conditionalImage?access_token=${state.accessToken}"

    return dynamicPage(name: "settings", title: "Settings", install: true){
        section("Device Setup") {
            input(name: "deviceTypeSelection", type: "enum", title: "Store URL in:", required: true, multiple: false, options: ["Custom Device", "Global Variable Connector"], submitOnChange: true, defaultValue: 'Custom Device', width: 5)
        }
        if (deviceTypeSelection == "Global Variable Connector") {
            section(getSectionTitle("Rule Machine Global Variable Connector")) {
                input name: "inputConnector", type: "capability.actuator", title:	"Rule Machine Global Variable Connector for Image URL", multiple: false, required: true
            }
        }
        section("") {
            paragraph("" + ((deviceTypeSelection == "Custom Device") ? "<li>A Dynamic Image URL Device has been created</li>" : "") + "<li>Configure different JPG image files at different URLs.</li><li>Use Rule Machine to set the " + ((deviceTypeSelection == "Global Variable Connector") ? "selected Global Variable Connector" : "Dynamic Image URL Device") + " to different ones of the URLs under different conditions.</li><li>The single URL below will dynamically serve the different JPGs under the different conditions.</li>")  
            paragraph("<ul><li><strong>Local</strong>:<a href='${localUri}'>${localUri}</a></li></ul>")     
        }

    }
}

def getSectionTitle(txt) {
     return '<strong>' + txt + '</strong>'   
}

mappings { 
    path("/conditionalImage") { action: [ GET: "getImage"] }
}

def installed() {
    initialize()
}

def initialize() {
    deleteChild()
    if (deviceTypeSelection == "Custom Device") {
        createChild()
    }
}

def updated()
{
    initialize()
}

def uninstalled()
{
    deleteChild()
}

def createChild()
{

    String childNetworkID = "DynamicImageUrlDevice${app.id}"
    child = addChildDevice("lnjustin", "Dynamic Image URL Device", childNetworkID, [label:"Dynamic Image URL Device", isComponent:true, name:"Dynamic Image URL Device"])
    child.updateSetting("parentID", app.id)
}

def deleteChild()
{
    deleteChildDevice("DynamicImageUrlDevice${app.id}")
}
    
def getURLFromChild() {
    def child = getChildDevice("DynamicImageUrlDevice${app.id}")
    if (child) {
        return child.getImageURL() 
    }
    log.error "No Child Device Found"
    return null
}

def getImage() {
	byte[] imageBytes = null
    String imageURL = ""
    
    if (deviceTypeSelection == "Custom Device") {
        imageURL = getURLFromChild()
    }
    else if (deviceTypeSelection == "Global Variable Connector") {
        imageURL = inputConnector?.currentValue("variable")
    }
    
    def params = [
        uri: imageURL,
        headers: ["Accept": "image/jpeg; image/svg+xml; image/png; image/gif"]
    ]
    try {
        httpGet(params) { resp ->
            if(resp?.data != null) {
                imageBytes = new byte[resp?.data.available()]
                resp?.data.read(imageBytes)
            }
            else {
                log.error "Null Response"
            }
        }
    } catch (exception) {
        log.error "Conditional Image Server exception: ${exception.message}"
    }

    String extension = "";
    int i = imageURL.lastIndexOf('.');
    if (i > 0) {
        extension = imageURL.substring(i+1).toLowerCase()
    }
    
    def contentTypeString = null    
    if (extension == "svg") {
        contentTypeString = "image/svg+xml"
    }
    else if (extension == "jpg" || extension == "jpeg") {
        contentTypeString = "image/jpeg"
    }
    else if (extension == "png") {
         contentTypeString = "image/png"
    }
    else if (extension == "gif") {
         contentTypeString = "image/gif"
    }
    render contentType: contentTypeString, data: imageBytes, status: 200
}
