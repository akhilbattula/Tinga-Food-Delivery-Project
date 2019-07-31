<?php 
session_start();

include "./config.php";
mysqli_set_charset($conn, 'utf8mb4');

//print '1';

if(isset($_POST['order_id'])){
//if($rid!=null){
//print "1";
    $rid = mysqli_real_escape_string($conn,htmlspecialchars($_POST['order_id']));
    //print $rid;
    $query = "SELECT * FROM `tbl_order` WHERE `order_id`=$rid";

    $result = mysqli_query($conn, $query);
    $res = array();
    
    if (!$result) {
        array_push($res,
          array('success'=>0,
                    'message'=>"Server issue. Please agin later.",
                    ));
                    
    }else{  
     
        $count = mysqli_num_rows($result);
        
        if($count === 0){
            array_push($res,
              array('success'=>0,
                    'message'=>"Order details not found.",
                    ));

        }else{
            $row = mysqli_fetch_array($result);
            
            $id = $row['order_id'];
            //print $id;
            $query1 = "SELECT *  FROM `tbl_order_Items` WHERE `order_id`=$rid";
            $result1 = mysqli_query($conn, $query1);
            $items_array = array();
            while($row_order_items = mysqli_fetch_array($result1)){
            
              $food_id = $row_order_items['food_id'];
              $query_food_details = "SELECT * FROM `tbl_food_items` WHERE `food_id`='$food_id'";

              $result_food_details = mysqli_query($conn, $query_food_details);
              $row_food_details = mysqli_fetch_array($result_food_details);
              
              $bean1  = array('food_id'=> $food_id,
                              'food_name'=>$row_food_details['name'],
                              'food_price'=>$row_food_details['price'],
                              'quantity'=>$row_order_items['quantity'],
                              'total_price'=>$row_order_items['price']
              );
              array_push($items_array,$bean1);
            }
            $rest_id = $row['restaurant_id'];
            $query_restaurant = "SELECT *  FROM `tbl_restaurants` WHERE `restaurant_id`='$rest_id'";
            
            $result_restaurant = mysqli_query($conn, $query_restaurant);
            $row_restaurant = mysqli_fetch_array($result_restaurant);
            
            $delivery_id = $row['delivery_id'];
            $query_delivery = "SELECT *  FROM `tbl_delivery` WHERE `delivery_id`=$delivery_id";
            
            $result_delivery = mysqli_query($conn, $query_delivery);
            $row_delivery = mysqli_fetch_array($result_delivery);
            
            
            $bean  = array('order_id'=> $row['order_id'],
                              'total_price'=>$row['total_price'],
                              'order_date'=>$row['order_date'],
                              'order_time'=>$row['order_time'],
                              'restaurant_id'=>$row['restaurant_id'],
                              'restaurant_name'=>$row_restaurant['name'],
                              'restaurant_image'=>$row_restaurant['image_path'],
                              'restaurant_address'=>$row_restaurant['address'],
                              'restaurant_city'=>$row_restaurant['city'],
                              'delivery_id'=>$row['delivery_id'],
                              'delivery_name'=>$row_delivery['delivery_name'],
                              
                              'order_rating'=>$row['order_rating'],
                              'delivery_rating'=>$row['delivery_rating'],
                              
                              'order_payment_mode'=>$row['order_payment_mode'],
                              'order_address'=>$row['order_address'],
                              'order_delivery_time'=>$row['order_delivery_time'],
                              'order_status'=>$row['order_status'],
                              'order_lat'=>$row['order_lat'],
                              'order_long'=>$row['order_long']
                                );
                          
               array_push($res,array("success"=>1,
                      'data' => $bean,'items' => $items_array));
      
         } 
    }
}
    

echo json_encode(array('response' => $res)); 