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
        section(getSectionTitle("Rule Machine Global Variable Connector")) {
            input name: "inputConnector", type: "capability.actuator", title:	"Rule Machine Global Variable Connector for Image URL", multiple: false, required: true
        }
        
        section("") {
            paragraph("<li>Configure different JPG image files at different URLs.</li><li>Use Rule Machine to set the selected Global Variable Connector to different URLs under different conditions.</li><li>The single URL below will dynamically serve the different JPGs under the different conditions.</li>")  
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

def getImage() {
	byte[] imageBytes = null
    def params = [
        uri: inputConnector.currentValue("variable"),
        headers: ["Accept": "image/jpeg"]
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
    render contentType: "image/jpeg", data: imageBytes, status: 200
}
