/**
 *  My Switch Calls Routines App
 *
 *  Copyright 2015 Anthony Pastor
 *  Version 1.0 10/28/15
 *
 *  Based IN LARGE PART on the "Big Switch for Hello Home Phrases" SmartApp by the esteemed Tim Slagle
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
 *  Ties a Hello, Home phrase to a switch's (virtual or real) on/off state. Perfect for use with IFTTT.
 *  Simple define a switch to be used, then tie the on/off state of the switch to a specific Hello, Home phrases.
 *  Connect the switch to an IFTTT action, and the Hello, Home phrase will fire with the switch state change.
 *
 *
 */

definition(
    name: "My Switch Calls Routines App",
    namespace: "info_fiend",
    author: "Anthony Pastor",
    description: "Child App to 'Parent App for Big Switch' that uses a virtual/or physical switch to run Routines.",
    category: "My Apps",
    parent: "info_fiend:Switch Mania",
    iconUrl: "http://icons.iconarchive.com/icons/icons8/windows-8/512/User-Interface-Switch-On-icon.png",
    iconX2Url: "http://icons.iconarchive.com/icons/icons8/windows-8/512/User-Interface-Switch-On-icon.png"
)




preferences {
	page(name: "getPref")
}
	
def getPref() {    
    dynamicPage(name: "getPref", title: "Choose Switch and Phrases", install:true, uninstall: true) {
    
    	def mySwitches = []
        mySwitches = parent.askForSwitches()
        log.debug "List of Switches from SwitchMania = ${mySwitches}"
        
	    section("Choose a switch to use...") {
			input "controlSwitch", "capability.switch", title: "Choose switch to use:", multiple: false, required: true
	    }
    	def phrases = location.helloHome?.getPhrases()*.label
			if (phrases) {
        		phrases.sort()
				section("Perform the following phrase when...") {
					log.trace phrases
					input "phrase_on", "enum", title: "Switch is on", required: false, options: phrases
					input "phrase_off", "enum", title: "Switch is off", required: false, options: phrases
				}
			}
		section([title:"Options", mobileOnly:true]) {
    	   	label title:"Assign a name", required:false
	    }
    
    }
}

def installed() {
	log.debug "Installed with settings: ${settings}"
	subscribe(controlSwitch, "switch", "switchHandler")
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
	subscribe(controlSwitch, "switch", "switchHandler")
}

def uninstalled() {
    removeChildDevices(getAllChildDevices())
}

private removeChildDevices(delete) {
    delete.each {
        deleteChildDevice(it.deviceNetworkId)
    }
}


def switchHandler(evt) {
	if (evt.value == "on") {
    	location.helloHome.execute(settings.phrase_on)
    } else {
    	location.helloHome.execute(settings.phrase_off)
    }
}

	