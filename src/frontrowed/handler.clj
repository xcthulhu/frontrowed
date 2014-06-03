(ns frontrowed.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [clojure.edn :as edn]
            [compojure.route :as route]
            [slingshot.slingshot :refer [throw+ try+]]))

(defn average
  "Compute the average of an ISeq"
  [nums]
  (let [length (count nums)]
    (when (not (zero? length))
      (loop [sum 0 [x & xs :as nums] nums]
        (cond
         (empty? nums) (/ sum length)
         (number? x) (recur (+ sum x) xs)
         :else (throw+ {:type ::not-a-number}))))))

(defn kv-average-of-averages
  "Compute the average of averages of a kv"
  [kv]
  {:average 
   (try+
    (average (map average (vals @kv)))
    (catch [:type ::not-a-number] {} nil))})

(defn kv-average
  "Compute the average of averages of a kv"
  [kv key]
  {:average
   (try+
    (average (get @kv key))
    (catch [:type ::not-a-number] {} nil))})

(defn kv-add-key!
  "Adds a value to a key-value store"
  [kv key value]
  (swap! kv update-in [(keyword key)]
         (partial cons
                  (edn/read-string value)))
  (get @kv (keyword key)))

(def ^:private kv (atom {}))

(defroutes app-routes
  (GET "/average-of-averages" []
       (str (kv-average-of-averages kv)))
  (GET "/key-average" {{:keys [:key]} :params}
       (str (kv-average kv key)))
  (POST "/add-key" {{:keys [:key :value]} :params}
        (str (kv-add-key! kv key value)))
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
