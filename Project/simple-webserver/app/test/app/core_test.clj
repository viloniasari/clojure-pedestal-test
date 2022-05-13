(ns app.core-test
  (:require [clojure.test :refer :all]
            [io.pedestal.test :refer :all]
            [io.pedestal.http :as http]
            [app.service :as service]))

(def service
  (::http/service-fn (http/create-servlet service/service)))

(deftest test-templates-generate-correct-bodies
  (are [url partial-body-string]
    (.contains (->> url
                    (response-for service :get)
                    :body)
               partial-body-string)
    "/hiccup"         "Hiccup"
    "/clostache"      "Clostache"
    "/stringtemplate" "String Template"
    "/comb"           "Comb"))
