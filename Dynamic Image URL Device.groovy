/**
 *  Dynamic Image URL Device
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
**/

metadata
{
    definition(name: "Dynamic Image URL Device", namespace: "lnjustin", author: "Justin Leonard", importUrl: "")
    {
        capability "Actuator"
        
        attribute "imageURL", "string"
        
        command "configureURL", ["string"]
    }
}

preferences
{
    section
    {
        input name: "parentID", type: "string", title: "Parent App ID"
        input name: "logEnable", type: "bool", title: "Enable debug logging", defaultValue: true
    }
}

def logDebug(msg) 
{
    if (logEnable)
    {
        log.debug(msg)
    }
}    

def updated()
{
    configure()
}

def parse(String description)
{
    logDebug(description)
}

def configure()
{

    state.clear()
    
    refresh()
}

def configureURL(url) {
    sendEvent(name: "imageURL", value: url)
    state.imageURL = url
}

def refresh()
{

}

def getImageURL()
{
    log.trace "Returning image URL from Child"
    return state.imageURL
}
