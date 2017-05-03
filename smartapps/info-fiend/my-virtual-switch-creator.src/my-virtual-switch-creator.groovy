/**
 *  My Virtual Switch (on/off or momentary) Creator
 *
 *  Copyright 2015 Anthony Pastor
 *  Based in LARGE part on "Virtual On/Off Switch Creator" by Eric Roberts
 *
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
 */
 
definition(
    name: "My Virtual Switch Creator",
    namespace: "info_fiend",
    author: "Anthony Pastor",
    description: "Child App that can create virtual on/off and momentary switches!",
    category: "My Apps",
    parent: "info_fiend:Switch Mania",
    iconUrl: "http://baldeagle072.github.io/icons/standard-tile@1x.png",
    iconX2Url: "http://baldeagle072.github.io/icons/standard-tile@2x.png",
    iconX3Url: "http://baldeagle072.github.io/icons/standard-tile@3x.png")


preferences {
	section("Create a Virtual Switch (Be sure to install all of the device types).") {
		input "switchLabel", "text", title: "Switch Label", required: true, submitOnChange: true
        input "switchType", "enum", title: "What kind of Virtual Switch do you want?", multiple: false, required: true, metadata: [values: ["On/Off Button Tile", "Momentary Button Tile", "Virtual Dimmer", "Simulated Presence Sensor"]], defaultValue: "On/Off Button Tile"  
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
    def deviceId = app.id + "SimulatedSwitch"
    log.debug(deviceId)
    def existing = getChildDevice(deviceId)

	if (!existing) {
    	if (switchType == "On/Off Button Tile") {
	    	def childDevice = addChildDevice("smartthings", "On/Off Button Tile", deviceId, null, [label: switchLabel])           
	    } else if (switchType == "Momentary Button Tile") {
			def childDevice = addChildDevice("MichaelStruck", "Momentary Button Tile", deviceId, null, [label: switchLabel])    
	    } else if (switchType == "Dimmer Switch") {
			def childDevice = addChildDevice("MichaelStruck", "Virtual Dimmer", deviceId, null, [label: switchLabel])    
	    } else if (switchType == "Simulated Presence Sensor") {
			def childDevice = addChildDevice("infofiend", "Simulated Presence Sensor", deviceId, null, [label: switchLabel])    
		}
	}
}

def uninstalled() {
    removeChildDevices(getAllChildDevices())
}

private removeChildDevices(delete) {
    delete.each {
        deleteChildDevice(it.deviceNetworkId)
    }
}