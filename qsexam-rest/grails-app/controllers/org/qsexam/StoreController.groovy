package org.qsexam

import grails.converters.JSON

class StoreController {
  def json() {
    JSON.use("deep") {
      render Store.findByName(params.name) as JSON
    }
  }
}
