(ns hello.core
  (:require [io.pedestal.http :as http]          ;; <2>
            [io.pedestal.http.route :as route]
            [ring.util.response :as ring-resp]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.interceptor :refer [interceptor]]
            [io.pedestal.http.route.definition :refer [defroutes]]
            [hiccup.page :as hp]
            [hiccup.core :as hc]))




(def common-interceptors
  ;; Parsing the body parameters according to the request’s content-type header.
  [(body-params/body-params) http/html-body])

(def button
  (hc/html [:button {:type "button"} "submit"]))

(def form-input
  (hc/html [:form {:action "/1"}
            [:label {:for "input-mess"} "input message"]
            [:br]
            [:input {:id "the-form-1" :type "text" :text "text"}]
            [:input {:type "submit" :value "Submit"}]]))

(def form-output
  (hc/html [:form
            [:label {:for "output-1"}]
            [:input {:id "the-output-1" :type "text" :name "text"}]
            [:br]
            [:label {:for "output-2"}]
            [:input {:id "the-output-2" :type "text" :name "text"}]
            [:br]
            [:label {:for "output-3"}]
            [:input {:id "the-output-3" :type "text" :name "text"}]
            [:br]
            [:label {:for "output-4"}]
            [:input {:id "the-output-4" :type "text" :name "text"}]]))

(defn hiccup-page
  [request]
  (ring-resp/response (hp/html5
                        [:div
                         [:h1 {:id "the-title"} "Web Pedestal"]
                         [:hr]
                         form-input
                         [:br]
                         form-output])))
(defn page-1
  [request]
  (ring-resp/response (hp/html5
                        [:div
                         [:h1 {:id "the-title"} "Web Pedestal"]
                         [:hr]
                         form-input
                         [:br]
                         form-output])))

;; end::response[]

;; tag::routing[]
(def routes
  (route/expand-routes                                   ;; <1>
    #{["/" :get (conj common-interceptors 'hiccup-page) :route-name :index]
      ["/1" :get (conj common-interceptors 'page-1) :route-name :page-1]})) ;; <2>
;; end::routing[]

;; tag::server[]
(defn create-server []
  (http/create-server     ;; <1>
    {::http/routes routes  ;; <2>
     ::http/type   :jetty  ;; <3>
     ::http/port   8080})) ;; <4>

(defn start []
  (http/start (create-server))) ;; <5>
;; end::server[]
