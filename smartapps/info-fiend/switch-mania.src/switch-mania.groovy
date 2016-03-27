/**
 *  Switch Mania
 *
 *  Copyright 2015 Anthony Pastor
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
    name: "Switch Mania",
    namespace: "info_fiend",
    author: "Anthony Pastor",
    description: "Parent App used with 2 Child Apps: (1) 'My Virtual Switch Creator' - create multiple virtual switches and (2) 'My Switch Calls Routines App' - call Routines (HH) when a switch is turned on or off.",
    category: "My Apps",
    iconUrl: "http://icons.iconarchive.com/icons/icons8/windows-8/512/User-Interface-Switch-On-icon.png",
    iconX2Url: "http://icons.iconarchive.com/icons/icons8/windows-8/512/User-Interface-Switch-On-icon.png",
    )


preferences {
	page name:"pageSwitches"
    page name:"pageRoutines"
   	page name:"pageOptions"    
}

def installed() {
	state.installed = true

    initialize()
    TRACE("installed()") 
}

def updated() {
	state.installed = true
//    unsubscribe()
    initialize()
    TRACE("updated()")
}

def initialize() {
	log.debug "PARENT:  there are ${childApps.size()} child Apps:"

	childApps.each {child ->
//    	log.debug "child.name is ${child.name}."
		if ( child.name == "My Virtual Switch Creator" ) {
			log.debug "child Virtual Switch: ${child.label}"
        } else if ( child.name == "My Switch Calls Routines App" ) {
	        log.debug "child Linking Switch and Routine: ${child.label}"        
        }    
    }
    
//	log.debug "${app.name} - PARENT:  there are ${childRoutines.size()} child Apps Linking Switches to Routines (HH Actions)."
    
//    childRoutines.each {child ->
//        log.debug "child Linking Switch and Routine: ${child.label}"
//    }
    
//	STATE()
}

private def	pageSwitches() {

    TRACE("pageSwitches()")
    
    if (state.installed == null) {
    // First run - initialize state
        state.installed = false
        return pageSwitches()
    }
    
	def pageProperties = [
        name        : "pageSwitches",
        title       : "Create and/or Edit Virtual Switches.",
                      
        nextPage    : "pageRoutines",
        install     : false,
        uninstall   : state.installed
	]
    
    return dynamicPage(pageProperties) {

		section {
			app(name: "childSwitches", appName: "My Virtual Switch Creator", namespace: "info_fiend", title: "Create a new virtual switch (Choose from On/Off Button Tile, Momentary Button Tile, Virtual Dimmer, or Simulated Presence Sensor).", multiple: true)
	    }
	}
}


private def	pageRoutines() {

    TRACE("pageRoutines()")
    
    if (state.installed == null) {
    // First run - initialize state
        state.installed = false
        return pageRoutines()
    }
    
	def pageProperties = [
        name        : "pageRoutines",
        title       : "Link Switches with Routines (HH Phrases).",
                      
        nextPage    : "pageOptions",
        install     : false,
        uninstall   : state.installed
	]
    
    return dynamicPage(pageProperties) {

		section {
			app(name: "childRoutines", appName: "My Switch Calls Routines App", namespace: "info_fiend", title: "Create new link between an existing switch and an existing Routine", multiple: true)
	    }
	}
}


private def pageOptions() {
	TRACE("pageOptions()")
   
	def pageProperties = [
        name        : "pageOptions",
        title       : "Options.",
        nextPage    : null,
        install     : true,
        uninstall   : state.installed
    ]

    return dynamicPage(pageProperties) {

        section([title:"Options", mobileOnly:true]) {
           	label title:"Assign a name", required:false
        }

	}
}


def askForSwitches() {

	def switchList = []
        switchList = getAllChildDevices()
    
    return switchList

}


private def TRACE(message) {
    log.debug message
}

private def STATE() {
    log.trace "settings: ${settings}"
    log.trace "state: ${state}"
}