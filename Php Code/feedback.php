<?php
session_start();

include("./config.php");
mysqli_set_charset($conn, 'utf8mb4');
$response = array();

  //echo "0";
if(isset($_POST['order_id']) && isset($_POST['foodrating']) && isset($_POST['deliveryrating']) && isset($_POST['restaurant_id']) && isset($_POST['delivery_id'])){
  
  //echo "1";

    $order_id = mysqli_real_escape_string($conn,htmlspecialchars($_POST['order_id']));
    //echo $uid;
    $foodrating = mysqli_real_escape_string($conn,htmlspecialchars($_POST['foodrating']));
    $deliveryrating = mysqli_real_escape_string($conn,htmlspecialchars($_POST['deliveryrating']));
    $restaurant_id = mysqli_real_escape_string($conn,htmlspecialchars($_POST['restaurant_id']));
    $delivery_id = mysqli_real_escape_string($conn,htmlspecialchars($_POST['delivery_id']));

    $date = date('Y-m-d H:i:s');
    
    $query_upload="INSERT INTO `tbl_ratings`(`keyword`, `rating`, `s_id`, `rd_id`, `created_on`) VALUES ('FOOD', $foodrating, '$order_id', '$restaurant_id','$date'), ('DELIVERY', $deliveryrating, '$order_id', '$delivery_id','$date')";
     
     if (mysqli_query($conn, $query_upload)) {
          
          $query_update="UPDATE `tbl_order` SET `order_rating`='$foodrating', `delivery_rating`='$deliveryrating' WHERE `order_id`=$order_id";
           
           if (mysqli_query($conn, $query_update)) {
           
                $query_restaurant = "SELECT * FROM `tbl_ratings` WHERE `rd_id`='$restaurant_id'";
              
                $result_restaurant = mysqli_query($conn, $query_restaurant);
                //$row_restaurant = mysqli_fetch_array($result_restaurant);

                $count_restaurant = mysqli_num_rows($result_restaurant);
                
                $rating_restaurant_sum = 0;
                
                while($row_restaurant = mysqli_fetch_array($result_restaurant)){
                    
                    $rating_restaurant_sum = $rating_restaurant_sum + (float)$row_restaurant['rating'];
                    
                }
                
                $restaurant_final_rating = $rating_restaurant_sum/$count_restaurant; 
                
                $query_delivery = "SELECT * FROM `tbl_ratings` WHERE `rd_id`='$delivery_id'";

                $result_delivery = mysqli_query($conn, $query_delivery);
                //$row_delivery = mysqli_fetch_array($result_delivery);
                
                $count_delivery = mysqli_num_rows($result_delivery);

                $rating_restaurant_sum = 0;
                
                while($row_delivery = mysqli_fetch_array($result_delivery)){
                    
                    $rating_delivery_sum = $rating_delivery_sum + (float)$row_delivery['rating'];
                    
                }
                
                $delivery_final_rating = $rating_delivery_sum/$count_delivery;
                
                $r_final = round($restaurant_final_rating,1);
                
                $d_final = round($delivery_final_rating,1);
                
                $query_rating_update1= "UPDATE `tbl_restaurants` SET `rating`='$r_final' WHERE `restaurant_id`='$restaurant_id'";
                
                $query_rating_update2= "UPDATE `tbl_delivery` SET `rating`='$d_final' WHERE `delivery_id`=$delivery_id";
           
                if (mysqli_query($conn, $query_rating_update1) && mysqli_query($conn, $query_rating_update2)) {
                    array_push($response,
                    array('success'=>1,
                          'message'=>"Feedback submitted successfully." ));

                }else{
                   array_push($response,
                      array('success'=>0,
                            'message'=>"Unable to place order due to " . mysqli_error($conn)
                            ));
                }
            
                
           } else {
           //echo "6";
           
               array_push($response,
                    array('success'=>0,
                          'message'=>"Unable to place order due to " . mysqli_error($conn)
                          ));
           }
           
     } else {
           //echo "6";
           
           array_push($response,
                array('success'=>0,
                      'message'=>"Unable to place order due to " . mysqli_error($conn)
                      ));
     }
         
               
}
echo json_encode(array('response' => $response));  