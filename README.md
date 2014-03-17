# stch.util

Utility fns for stch-library.

## How to use

Add the following to your project dependencies:

```Clojure
[stch-library/util "0.1.0"]
```

## API Documentation

http://stch-library.github.io/util

## Examples

```Clojure
(use 'stch.util)

(with-private-fns [org.foo.bar [fn1 fn2]]
  (fn1)
  (fn2))

(->map [[:name "Billy"][:age 35]])
; {:name "Billy" :age 35}

(named? "Billy") ; true
(named? :Billy) ; true
(named? 'Billy) ; true

(def num-or-str (or-fn number? string?))
(num-or-str 3) ; true
(num-or-str "3") ; true

(def odd-num (and-fn number? odd?))
(odd-num 3) ; true

(dissoc-vec [1 2 3] 1)
; [1 3]

(dissoc-in {:name "Billy" :age 35} [:age])
; {:name "Billy"}

(indexed '(a b c d))
; '([0 a] [1 b] [2 c] [3 d])

(positions even? [0 1 2])
; '(0 2)

(in? 3 [2 3 4])
; true

(first-index 3 [2 3 4 3])
; 1

(deep-merge {:a 1 :b {:c 1 :d 1}}
            {:a 2 :b {:c 2}})
; {:a 2, :b {:c 2, :d 1}}

(deep-merge-with +
  {:a {:b {:c 1 :d {:x 1 :y 2}} :e 3} :f 4}
  {:a {:b {:c 2 :d {:z 9} :z 3} :e 100}})
; {:a {:b {:z 3, :c 3, :d {:z 9, :y 2, :x 1}}, :e 103}, :f 4}
```















