(ns clj-aws-sign.core-test
  (:require [clj-aws-sign.core :as sut]
            [clj-aws-sign.utils :as u]
            [clojure.test :refer :all]
            [clojure.string :as s]))

;; Directory where the test suite lives
(def test-suite-dir  "suite")

;; Test data, global in whole test suite
(def timestamp "20150830T123600Z")
(def short-timestamp "20150830")
(def region "us-east-1")
(def service "service")
(def secret-key "wJalrXUtnFEMI/K7MDENG+bPxRfiCYEXAMPLEKEY")
(def access-key-id "AKIDEXAMPLE")

(def test-requests '("get-header-key-duplicate"
                     "get-header-value-multiline"
                     "get-header-value-order"
                     "get-header-value-trim"
                     "get-unreserved"
                     "get-utf8"
                     "get-vanilla"
                     "get-vanilla-empty-query-key"
                     "get-vanilla-query"
                     "get-vanilla-query-order-key"
                     "get-vanilla-query-order-key-case"
                     "get-vanilla-query-order-value"
                     "get-vanilla-query-unreserved"
                     "get-vanilla-utf8-query"
                     "post-header-key-case"
                     "post-header-key-sort"
                     "post-header-value-case"
                     "post-sts-header-before"
                     "post-sts-header-after"
                     "post-vanilla"
                     "post-vanilla-empty-query-value"
                     "post-vanilla-query"
                     "post-x-www-form-urlencoded"
                     "post-x-www-form-urlencoded-parameters"
                     "get-relative"
                     "get-relative-relative"
                     "get-slash"
                     "get-slash-dot-slash"
                     "get-slashes"
                     "get-slash-pointless-dot"
                     "get-space"))

(deftest smoke-test
  (is (+ 2 2) 4))

(deftest signature-tests
  (testing "AWS V4 signature:"
    (doseq [name test-requests]
      (testing (str "creating canonical request for " name)
        (let [input-string (u/resource-string test-suite-dir name "req")
              output-string (u/resource-string test-suite-dir name "creq")
              {:keys [method uri headers query payload]} (u/parse-request input-string)]
          (is
           (= output-string
              (sut/aws4-auth-canonical-request method uri query payload (sut/aws4-auth-canonical-headers headers)))
           (str "returns valid canonical request for " name))))

      (testing (str "creating string-to-sign for " name)
        (let [input-string (u/resource-string test-suite-dir name "req")
              output-string (u/resource-string test-suite-dir name "sts")
              {:keys [method uri headers query payload]} (u/parse-request input-string)
              canonical-headers (sut/aws4-auth-canonical-headers headers)]
          (is
           (= output-string
              (sut/string-to-sign timestamp method uri query payload short-timestamp
                                  region service canonical-headers))
           (str "returns valid string-to-sign for " name))))
      (testing (str "creating signature for " name)
        (let [input-string (u/resource-string test-suite-dir name "req")
              output-string (u/parse-signature (u/resource-string test-suite-dir name "authz"))
              {:keys [method uri headers query payload]} (u/parse-request input-string)
              canonical-headers (sut/aws4-auth-canonical-headers headers)
              string-to-sign (sut/string-to-sign timestamp method uri query payload
                                                 short-timestamp region service canonical-headers)]
          (is
           (= output-string
              (sut/signature secret-key short-timestamp region service string-to-sign))
           (str "returns valid signature for " name))))

      (testing (str "creating authorization header for " name)
        (let [input-string (u/resource-string test-suite-dir name "req")
              {:keys [method uri headers query payload]} (u/parse-request input-string)
              output-string (u/resource-string test-suite-dir name "authz")]
          (is
           (= output-string
              (sut/aws4-authorisation method uri query headers payload region service access-key-id secret-key))
           (str "returns valid authorization header for " name)))))))
