(ns stch.util-spec
  (:use speclj.core stch.util))

(describe "stch.util"
  (it "with-private-fns"
    (with-private-fns [stch.util [named?]]
      (should-be true? (named? "Billy"))))
  (it "heap-mb"
    (should-be (and-fn number? pos?) (heap-mb)))
  (context "named?"
    (it "string"
      (should-be true? (named? "Billy")))
    (it "keyword"
      (should-be true? (named? :Billy)))
    (it "symbol"
      (should-be true? (named? 'Billy))))
  (it "->map"
    (should= {:name "Billy" :age 35}
             (->map [[:name "Billy"][:age 35]])))
  (context "and'"
    (context "one arg"
      (it "true"
        (should= true (and' true)))
      (it "false"
        (should= false (and' false))))
    (context "two args"
      (it "true"
        (should= 2 (and' 1 2)))
      (it "false"
        (should-be-nil (and' 1 nil))))
    (context "four args"
      (it "true"
        (should= 4 (and' 1 2 3 4)))
      (it "false"
        (should-be-nil (and' 1 nil 2 3)))))
  (context "or'"
    (context "one arg"
      (it "true"
        (should= true (or' true)))
      (it "false"
        (should= false (or' false))))
    (context "two args"
      (it "true"
        (should= 1 (or' 1 2)))
      (it "false"
        (should-be-nil (or' nil nil))))
    (context "four args"
      (it "true"
        (should= 1 (or' 1 2 3 4)))
      (it "false"
        (should= 2 (or' nil nil 2 3)))))
  (context "or-fn"
    (context "one pred"
      (it "true"
        (should= true ((or-fn number?) 3)))
      (it "false"
        (should= false ((or-fn number?) "3"))))
    (context "two preds"
      (it "true"
        (should= true ((or-fn number? odd?) 3)))
      (it "false"
        (should= false ((or-fn number? string?) :billy))))
    (context "four preds"
      (it "true"
        (should= true ((or-fn number? odd? even? string?) 3)))
      (it "false"
        (should= false ((or-fn number? string? symbol? keyword?)
                        #inst "2014-01-01")))))
  (context "and-fn"
    (context "one pred"
      (it "true"
        (should= true ((and-fn number?) 3)))
      (it "false"
        (should= false ((and-fn number?) "3"))))
    (context "two preds"
      (it "true"
        (should= true ((and-fn number? odd?) 3)))
      (it "false"
        (should= false ((and-fn number? even?) 3))))
    (context "four preds"
      (it "true"
        (should= 3 ((and-fn number? odd? #{3} #{3 4 5})
                    3)))
      (it "false"
        (should= false ((and-fn number? even? #{3} #{3 4 5})
                    3)))))
  (context "dissoc-vec"
    (context "invalid index"
      (it "empty vec"
        (should-throw (dissoc-vec [] 0)))
      (it "outside range"
        (should-throw (dissoc-vec [0 1 2] 3))))
    (it "index: 0"
      (should= [2 3] (dissoc-vec [1 2 3] 0)))
    (it "index: 1"
      (should= [1 3] (dissoc-vec [1 2 3] 1))))
  (context "dissoc-in"
    (it "one level"
      (should= {:name "Billy"}
               (dissoc-in {:name "Billy" :age 35}
                          [:age])))
    (it "two levels"
      (should= {:address {:street "Main St."}}
               (dissoc-in {:address
                           {:street "Main St."
                            :city "Podunk"}}
                          [:address :city]))))
  (it "indexed"
    (should= '([0 a] [1 b] [2 c] [3 d])
             (indexed '(a b c d))))
  (it "positions"
    (should= '(0 2)
             (positions even? [0 1 2])))
  (context "in?"
    (it "empty"
      (should-be-nil (in? 3 [])))
    (it "true"
      (should= true
               (in? 3 [2 3 4])))
    (it "false"
      (should-be-nil (in? 3 [4 5 6]))))
  (context "first-index"
    (it "empty"
      (should-be-nil (first-index 3 [])))
    (it "true"
      (should= 1
               (first-index 3 [2 3 4 3])))
    (it "false"
      (should-be-nil (first-index 3 [4 5 6]))))
  (it "deep-merge"
    (should= {:a 2, :b {:c 2, :d 1}}
             (deep-merge {:a 1 :b {:c 1 :d 1}}
                         {:a 2 :b {:c 2}})))
  (it "deep-merge-with"
    (should= {:a {:b {:z 3, :c 3, :d {:z 9, :y 2, :x 1}}, :e 103}, :f 4}
             (deep-merge-with +
               {:a {:b {:c 1 :d {:x 1 :y 2}} :e 3} :f 4}
               {:a {:b {:c 2 :d {:z 9} :z 3} :e 100}}))))

















