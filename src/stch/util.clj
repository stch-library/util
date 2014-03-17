(ns stch.util
  "Utility functions.")

(defmacro with-private-fns
  "Refers private fns from ns and executes body.

  (with-private-fns [org.foo.bar [fn1 fn2]]
    (fn1)
    (fn2))"
  [[ns vars] & body]
  `(let ~(reduce #(conj %1 %2 `(ns-resolve '~ns '~%2)) [] vars)
     ~@body))

(defn heap-mb
  "Report approx heap size in megabytes."
  []
  (System/gc)
  (quot (- (.totalMemory (Runtime/getRuntime))
           (.freeMemory (Runtime/getRuntime)))
        1000000))

(defn named?
  "Can the value be passed to name?"
  [x]
  (or (string? x)
      (instance? clojure.lang.Named x)))

(defn ->map
  "Convert a collection to a hash map."
  [coll]
  (into {} coll))

(defn and'
  "Functional equivalent of and. All arguments are
  evaluated, but not necessarily checked."
  ([x] x)
  ([x y] (and x y))
  ([x y z] (and x y z))
  ([x y z & forms]
   (and x y z
    (loop [[f & rst] forms]
      (if f
        (if rst
          (recur rst)
          f)
        f)))))

(defn or'
  "Functional equivalent of or. All arguments are
  evaluated, but not necessarily checked."
  ([x] x)
  ([x y] (or x y))
  ([x y z] (or x y z))
  ([x y z & forms]
   (or x y z
    (loop [[f & rst] forms]
      (if f f
        (if rst
          (recur rst)
          f))))))

(defn or-fn
  "Takes one or more predicates, returns a
  function that takes a value and applies each
  predicate to that value, and returns the first
  returned value that is truthy. Behavior is similar
  to some-fn."
  ([p1]
   (fn [x]
     (p1 x)))
  ([p1 p2]
   (fn [x]
     (or (p1 x)
         (p2 x))))
  ([p1 p2 p3]
   (fn [x]
     (or (p1 x)
         (p2 x)
         (p3 x))))
  ([p1 p2 p3 & ps]
   (fn [x]
     (or (p1 x)
         (p2 x)
         (p3 x)
         (loop [[p & rst] ps]
           (let [y (p x)]
             (if y y
               (if rst
                 (recur rst)
                 y))))))))

(defn and-fn
  "Takes one or more predicates and returns a
  function that takes a value, applies each
  predicate to that value, and returns the result of
  the last predicate if all predicates return a truthy
  value."
  ([p1]
   (fn [x]
     (p1 x)))
  ([p1 p2]
   (fn [x]
     (and (p1 x)
          (p2 x))))
  ([p1 p2 p3]
   (fn [x]
     (and (p1 x)
          (p2 x)
          (p3 x))))
  ([p1 p2 p3 & ps]
   (fn [x]
     (and (p1 x)
          (p2 x)
          (p3 x)
          (loop [[p & rst] ps]
            (let [y (p x)]
              (if y
                (if rst
                  (recur rst)
                  y)
                y)))))))

(def empty-queue clojure.lang.PersistentQueue/EMPTY)

(defn dissoc-vec
  "Remove the element at index i and return a new
  vector.  Will throw if index does not exist."
  [v i]
  (if (zero? i)
    (subvec v 1)
    (vec (concat (subvec v 0 i)
                 (subvec v (inc i))))))

(defn dissoc-in
  "Dissociates an entry from a nested associative
  structure returning a new nested structure. keys is
  a sequence of keys. Any empty maps that result
  will not be present in the new structure."
  [m [k & ks]]
  (if ks
    (if-let [nextmap (get m k)]
      (let [newmap (dissoc-in nextmap ks)]
        (if (seq newmap)
          (assoc m k newmap)
          (dissoc m k)))
      m)
    (dissoc m k)))

(defn indexed
  "Returns a lazy sequence of [index, item] pairs,
  where items come from s and indexes count up from zero.

  (indexed '(a b c d)) => ([0 a] [1 b] [2 c] [3 d])"
  [s]
  (map-indexed vector s))

(defn positions
  "Returns a lazy sequence containing the positions
  at which pred is true for items in coll."
  [pred coll]
  (for [[idx elt] (indexed coll)
        :when (pred elt)]
    idx))

(defn in?
  "Is needle in coll?"
  [needle coll]
  (when (seq coll)
    (or (= needle (first coll))
        (recur needle (next coll)))))

(defn first-index
  "Returns the first index where needle
  is found in coll."
  [needle coll]
  (loop [index 0
         needle needle
         coll coll]
    (when (seq coll)
      (if (= needle (first coll))
        index
        (recur (inc index) needle (next coll))))))

(defn deep-merge
  "Merges maps recursively, returning the last item
  when there's a non-map at a particular level.
  (deep-merge {:a 1 :b {:c 1 :d 1}} {:a 2 :b {:c 2}})"
  [& maps]
  (if (every? map? maps)
    (apply merge-with deep-merge maps)
    (last maps)))

(defn deep-merge-with
  "Like merge-with, but merges maps recursively,
  applying the given fn only when there's a non
  map at a particular level.

  (deep-merge-with + {:a {:b {:c 1 :d {:x 1 :y 2}} :e 3} :f 4}
                     {:a {:b {:c 2 :d {:z 9} :z 3} :e 100}})
  => {:a {:b {:z 3, :c 3, :d {:z 9, :x 1, :y 2}}, :e 103}, :f 4}"
  [f & maps]
  (apply (fn m [& maps]
           (if (every? map? maps)
             (apply merge-with m maps)
             (apply f maps)))
         maps))









