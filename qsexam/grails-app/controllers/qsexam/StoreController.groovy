package qsexam

import grails.converters.JSON

class StoreController {

	def show() {
		JSON.use("deep") {
			def store = null;
			if (params.q) {
				store = Store.findByName(params.q)
			}
			if (params.callback) {
				render(contentType: "text/javascript", text: "${params.callback}(${store.encodeAsJSON()})")
			}
			else {
				render store as JSON
			}
		}
	}
}
