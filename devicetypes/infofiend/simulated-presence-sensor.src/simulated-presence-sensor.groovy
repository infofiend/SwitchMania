/*  Simulated Presence Sensor 
 *
 * Taken from SmartThings base code, enhanced and bugfixed by RBoy
 *
 */

 metadata {
	// Automatically generated. Make future change here.
	definition (name: "Simulated Presence Sensor", namespace: "infofiend", author: "smartthings") {
		capability "Presence Sensor"
		capability "Sensor"
		capability "Actuator"

		command "arrived"
		command "departed"
        command "away"
        command "present"
	}

	simulator {
		status "present": "presence: 1"
		status "not present": "presence: 0"
	}

	tiles {
		standardTile("presence", "device.presence", width: 2, height: 2, canChangeBackground: true, inactiveLabel: false, canChangeIcon: true) {
			state("present", label:'${name}', icon:"st.presence.tile.mobile-present", action:"departed", backgroundColor:"#53a7c0")
			state("not present", label:'${name}', icon:"st.presence.tile.mobile-not-present", action:"arrived", backgroundColor:"#CCCC00")
		}
		main "presence"
		details "presence"
	}
}

def parse(String description) {
	def name = parseName(description)
	def value = parseValue(description)
	def linkText = getLinkText(device)
	def descriptionText = parseDescriptionText(linkText, value, description)
	def handlerName = getState(value)
	def isStateChange = isStateChange(device, name, value)

	def results = [
		name: name,
		value: value,
		unit: null,
		linkText: linkText,
		descriptionText: descriptionText,
		handlerName: handlerName,
		isStateChange: isStateChange,
		displayed: displayed(description, isStateChange)
	]
	log.debug "Parse returned $results.descriptionText"
	return results

}

private String parseName(String description) {
	if (description?.startsWith("presence: ")) {
		return "presence"
	}
	null
}

private String parseValue(String description) {
	switch(description) {
		case "presence: 1": return "arrived"
		case "presence: 0": return "departed"
        
		default: return description
	}
}

private parseDescriptionText(String linkText, String value, String description) {
	switch(value) {
		case "present": return "$linkText has arrived"
		case "not present": return "$linkText has departed"
        case "arrived": return "$linkText has arrived"
		case "departed": return "$linkText has departed"
		default: return value
	}
}

private getState(String value) {
	switch(value) {
		case "present": return "arrived"
		case "not present": return "departed"
		default: return value
	}
}



// handle commands
def arrived() {
	log.trace "Executing 'arrived'"
	sendEvent(name: "presence", value: "present")
}


def departed() {
	log.trace "Executing 'Departed'"
	sendEvent(name: "presence", value: "not present")
}

def away() {
	log.trace "Executing 'Away'"
	sendEvent(name: 'presence', value: 'not present')
}

def present() {
	log.trace "Executing 'Presence'"
	sendEvent(name: 'presence', value: 'present')
}