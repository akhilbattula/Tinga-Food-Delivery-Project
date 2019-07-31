<?php 
session_start();

include "../config.php";
mysqli_set_charset($conn, 'utf8mb4');

$res = array();

if(isset($_POST['rid'])){

    $rid = mysqli_real_escape_string($conn,htmlspecialchars($_POST['rid']));
    
    $query = "SELECT * FROM `tbl_order` WHERE `restaurant_id`='$rid' ORDER BY `created_on` DESC";

    $result = mysqli_query($conn, $query);
    
    if (!$result) {
        array_push($res,
          array('success'=>0,
                    'message'=>"Server issue. Please agin later."
                    ));
                    
    }else{  
     
        $count = mysqli_num_rows($result);
        
        if($count === 0){
            array_push($res,
              array('success'=>0,
                    'message'=>"There are no previous orders in the database."
                    ));

        }else{
                    
            while($row = mysqli_fetch_array($result)){
            
                
                  $id = $row['customer_id'];
                  
                  $query1 = "SELECT * FROM tbl_users WHERE `uid`='$id'";
                  $result1 = mysqli_query($conn, $query1);
                  $row_customer = mysqli_fetch_array($result1);
                
                  //print $id;
                  $order_id = $row['order_id'];
                  $query_items = "SELECT *  FROM `tbl_order_Items` WHERE `order_id`=$order_id";
                  $result_items = mysqli_query($conn, $query_items);
                  $items_array = array();
                  while($row_order_items = mysqli_fetch_array($result_items)){
                  
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
                  
                  $name = $row_customer['fname']." ".$row_customer['lname'];
                  
                  $bean  = array('order_id'=> $row['order_id'],
                                  'total_price'=>$row['total_price'],
                                  'order_status'=>$row['order_status'],
                                  'order_date'=>$row['order_date'],
                                  'order_time'=>$row['order_time'],
                                  'restaurant_id'=>$row['restaurant_id'],
                                  'restaurant_name'=>$row_restaurant['name'],
                                  'restaurant_image'=>$row_restaurant['image_path'],
                                  'restaurant_address'=>$row_restaurant['address'],
                                  'restaurant_city'=>$row_restaurant['city'],
                                  'customer_name'=>$name,
                                  'customer_id'=>$id,
                                  'food_items'=>$items_array
                                  );
                            
                            
                 array_push($res,array("success"=>1,
                        array("data" => $bean)
                                ));
            
             
                        
            }
          
        }
    }
}
  echo json_encode(array('response' => $res)); 
