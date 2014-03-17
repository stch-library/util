(defproject stch-library/util "0.1.1"
  :description "Utility fns for stch-library."
  :url "https://github.com/stch-library/util"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :profiles {:dev {:dependencies [[speclj "3.0.2"]]}}
  :plugins [[speclj "3.0.2"]
            [codox "0.6.7"]]
  :codox {:src-dir-uri "https://github.com/stch-library/util/blob/master/"
          :src-linenum-anchor-prefix "L"}
  :test-paths ["spec"])
