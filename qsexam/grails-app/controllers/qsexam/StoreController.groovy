package qsexam

import grails.converters.JSON

class StoreController {

	def json() {
		JSON.use("deep") {
			if (params.q) {
				render Store.findByName(params.q) as JSON
			}
			else {
				render null as JSON
			}
		}
	}
}
