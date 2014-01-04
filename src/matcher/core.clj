(ns matcher.core)

(def weights [3 3 1 2 4])

(defn person [n i e] {:name n :interests i :email e})

(defn professor [n i e] (person n i e))

(defn student [n i e] (person n i e))

(def will (professor "Will Guiford" [1 1 0 1 0] "will@virginia.edu"))

(def john (professor "John Schmone" [0 1 1 0 1] "hello@virginia.edu"))

(def professor-list [will john])

(def brent (student "Brent Baumgartner" [0 1 1 0 1] "bwb8ta@virginia.edu"))

(defn score [prof stud]
  (let [s1 (prof :interests)
        s2 (stud :interests)
        s3 weights]
  (reduce + (map * s1 s2 s3))))

(score will brent)
(score john brent)

(defn recommend [stud] (apply max-key (partial score stud) professor-list))
(recommend brent)
(+ 2 2)
