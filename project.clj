(defproject clj-aws-sign "0.1.1"
  :description "Library for signing AWS requests using signature V4"
  :url "http://www.erben.io"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.3"]]
  :resource-paths ["resources" "target/resources"]
  :profiles {:dev {:dependencies [[cider/piggieback "0.5.3"]]
                   :repl-options {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}}})
