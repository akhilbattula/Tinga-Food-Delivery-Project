<?php 
session_start();

include "./config.php";
mysqli_set_charset($conn, 'utf8mb4');

$res = array();

if(isset($_POST['uid'])){

    $rid = mysqli_real_escape_string($conn,htmlspecialchars($_POST['uid']));

    
    $query = "SELECT * FROM `tbl_order` WHERE `customer_id`='$rid' ORDER BY `created_on` DESC";

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
          
            while($row = mysqli_fetch_array($result))
            {
            
              $id = $row['restaurant_id'];
              
          $query1 = "SELECT `restaurant_id`, `name`, `address`, `city`, `image_path`  FROM `tbl_restaurants` WHERE `restaurant_id`='$id'";
          $result1 = mysqli_query($conn, $query1);
          $row_restaurant = mysqli_fetch_array($result1);
              
              $bean  = array('order_id'=> $row['order_id'],
                              'customer_id'=>$row['customer_id'],
                              'total_price'=>$row['total_price'],
                              'order_status'=>$row['order_status'],
                              'order_date'=>$row['order_date'],
                              'order_time'=>$row['order_time'],
                              'restaurant_name'=>$row_restaurant['name'],
                              'restaurant_address'=>$row_restaurant['address'],
                              'restaurant_city'=>$row_restaurant['city'],
                              'restaurant_image'=>$row_restaurant['image_path']
                              );
                        
             array_push($res,array("success"=>1,
                    array("data" => $bean)
                            ));
        
            }
          
        }
    }



}

echo json_encode(array('response' => $res)); 