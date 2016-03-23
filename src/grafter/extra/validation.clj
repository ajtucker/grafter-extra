(ns grafter.extra.validation
  (:require [grafter.rdf.repository :refer [->connection query]]))

(defn- ask [repository ask-query]
  (with-open [connection (->connection repository)]
    (let [result (query connection ask-query)]
      result)))

(defn constraint-asserter [invalid? ask-query message]
  "Returns a function for testing that a condition, specified by the ask-query,
   holds for the data in a given repository.

   The definition of an invalid response is given by invalid?"
  
  (fn [repository]
    (if (invalid? (ask repository ask-query))
      (throw (AssertionError. (str "Constraint violated: " message))))))

(defn absence-asserter [ask-query message]
  "Throws an AssertionError if matching data is found (i.e. the ASK returns true)"
  (constraint-asserter true? ask-query message))

(defn presence-asserter [ask-query message]
  "Throws an AssertionError unless matching data is found (i.e. the ASK returns false)"
  (constraint-asserter false? ask-query message))
