(ns frontrowed.test.handler
  (:use clojure.test
        ring.mock.request  
        frontrowed.handler))

(deftest kv-tests
  (let [kv (atom {})]
    (testing "kv is empty"
      (is (empty? (vals @kv))))

    (testing "kv-add! adds items to an empty kv"
      (is (= '(1) (kv-add-key! kv "test" "1")))
      (is (not (empty? (vals @kv)))))
    
   (testing "kv-add-key! retains items in kv"
     (kv-add-key! kv "test" "2")
     (kv-add-key! kv "test2" "5/2")
     (is (= 2 (count (:test @kv))))
     (is (= 5/2 (first (:test2 @kv)))))
   
   (testing "kv-average can get the average for a key"
     (is (= {:average 3/2} (kv-average kv :test))))
   
   (testing "kv-average-of-averages can get the average of averages"
     
     (is (= {:average 2} (kv-average-of-averages kv))))))

(deftest test-app
  (testing "add-key"
    (let [response (app (request :post "/add-key" 
                                 {:key "test" :value 1}))]
      (is (= (:status response) 200))
      (is (= (:body response) (str '(1))))))
  
  (testing "average-of-averages"
    (let [response (app (request :get "/average-of-averages"))]
      (is (= (:status response) 200))
      (is (= (:body response) (str {:average 1}))))) 
  
  (testing "key-average"
    (let [response (app (request :get "/average-of-averages"
                                 {:key "test"}))]
      (is (= (:status response) 200))
      (is (= (:body response) (str {:average 1}))))) 
  
  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= (:status response) 404))))
  )
