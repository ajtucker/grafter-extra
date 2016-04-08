(ns grafter.extra.validation.pmd-test
  (:require [clojure.test :refer :all]
            [grafter.extra.repository :refer [with-repository-containing]]
            [grafter.extra.validation.pmd :refer :all]))

(defn includes? [strings string]
  (some (partial re-find (re-pattern string)) strings))

(deftest errors-test
  (let [expected (list "is not a pmd:Dataset"
                       "is missing a pmd:graph"
                       "is missing a dcterms:title")
        invalid (with-repository-containing [r "./test/resources/cube-bad.ttl"]
                  (doall (errors r "http://example.org/ns#dataset-le3")))
        valid (with-repository-containing [r "./test/resources/outdoor-visits.nt"]
                (doall (errors r "http://statistics.gov.scot/data/outdoor-visits")))]
    (doseq [message expected]
      (is (includes? invalid message))
      (is (not (includes? valid message))))))

(deftest omissions-test
  (let [expected (list "is missing a dcterms:modified")
        missing (with-repository-containing [r "./test/resources/cube-bad.ttl"]
                  (doall (omissions r "http://example.org/ns#dataset-le3")))
        present (with-repository-containing [r "./test/resources/outdoor-visits.nt"]
                  (doall (omissions r "http://statistics.gov.scot/data/outdoor-visits")))]
    (doseq [message expected]
      (is (includes? missing message))
      (is (not (includes? present message))))))
