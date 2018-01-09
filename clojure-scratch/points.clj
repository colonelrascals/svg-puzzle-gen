;;; clojure -i points.clj > points.svg

(ns svgs)

(defn svg-prefix [width height]
  (let [prefix-string "<svg xmlns=\"http://www.w3.org/2000/svg\"
  xmlns:xlink=\"http://www.w3.org/1999/xlink\"
  width=\"%s\" height=\"%s\">"]
    (format prefix-string width height)
  )
)

(def svg-suffix "</svg>")

(defn ellipse [color width height]
  (let [
    cx (/ width 2.0)
    cy (/ height 2.0)
    rx (* 0.7 (/ width 2.0))
    ry (* 0.7 (/ height 2.0))
    ellipse-string "<ellipse
      cx=\"%.1f\"
      cy=\"%.1f\"
      rx=\"%.1f\"
      ry=\"%.1f\"
      stroke=\"#fc8d62\"
      stroke-width=\"5\"
      fill=\"#%s\"
    />"]
    (format ellipse-string cx cy rx ry color))
)

(defn point [x y]
  (let [
    radius 3
    ellipse-string "<ellipse
      cx=\"%d\"
      cy=\"%d\"
      rx=\"%d\"
      ry=\"%d\"
      stroke=\"#fc8d62\"
      stroke-width=\"5\"
    />"]
    (format ellipse-string x y radius radius))
)

(defn x [point] (nth point 0))
(defn y [point] (nth point 1))

(defn puzzlepath [point1 point2]
  (let [
    x1 (x point1)
    y1 (y point1)
    x2 (x point2)
    y2 (y point2)
    dx (- x2 x1)
    dy (- y2 y1)
    line-length (Math/sqrt (+ (* dx dx) (* dy dy)))
    angle (* (/ 180 Math/PI) (Math/atan2 dy dx))
    path-string "<path 
        d=\"M0,21 C30,28 45,28 45,21 C45,17 34,16 35,11 C36,6 42.5,1 50,1 C57.5,1 64,6 65,11 C66,16 55,20 55,21 C55,24 70,24 100,21\"
        id=\"path-%d-%d-%d-%d\"
        stroke=\"#979797\"
        fill=\"none\"
        transform=\"translate(%d %d) rotate(%f 0 21) scale(%f 1)\">
      </path>"
    ]
    (format path-string x1 y1 x2 y2 x1 (- y1 21) angle (/ line-length 100)))
)

(defn flippable-puzzle-path [p1 p2]
  (puzzlepath p1 p2))


(defn rand-color []
  (let [colors ["66c2a5" "fc8d62" "8da0cb"]]
    (nth colors (rand-int (count colors)))
    ))

(def max-coord 1000)
(def N 3)

(defn svg [body]
  (println (svg-prefix max-coord max-coord))
  (println (apply str body))
  (println svg-suffix))

(def coords
  (vec (for [x (range N)]
    (vec (for [y (range N)] 
      (let [xcoord (+ 50 (* 100 x))
            ycoord (+ 50 (* 100 y))
            xjitter (rand-int 20)
            yjitter (rand-int 20)
        ] (vec (list (+ xcoord xjitter) (+ ycoord yjitter)))))))))

(svg 
(flatten (for [i (range N) j (range N)]
  (let [point1 (nth (nth coords i) j)
       path-south (if (< (+ 1 j) N) (flippable-puzzle-path point1  (nth (nth coords i) (+ 1 j))))
       path-east (if (< (+ 1 i) N) (flippable-puzzle-path point1 (nth (nth coords (+ 1 i)) j)))
  ]
    (filter (complement nil?) (list path-south path-east))
  ))))

; (println coords)

; (svg (list 
;   (puzzlepath [50 50] [150 50])
;   (puzzlepath [50 50] [50 150])
;   (puzzlepath [50 50] [150 150])
;   (puzzlepath [150 150] [50 50])
;   ))





