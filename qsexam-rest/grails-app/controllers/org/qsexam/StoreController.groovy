package org.qsexam

import grails.converters.JSON

class StoreController {
  def json() {
    JSON.use("deep") {
      if (params.name) {
        render Store.findByName(params.name) as JSON
      }
      else {
        render null as JSON
      }
    }
  }
}
