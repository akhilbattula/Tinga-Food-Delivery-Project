<?php
session_start();

include("./config.php");
mysqli_set_charset($conn, 'utf8mb4');
$response = array();

	define('SMSUSER','your user name');
	define('PASSWORD','your password');
	define('SENDERID','EMPIRE');

//This function will send the otp 
function sendOtp($id, $phone){
  	//This is the sms text that will be sent via sms 
  	$sms_content = "You have a new order with order id #.$id. Please check the details in the app...";
  	
  	//Encoding the text in url format
  	$sms_text = urlencode($sms_content);
  
  	//This is the Actual API URL concatnated with required values 
   
  	$api_url = 'https://www.fast2sms.com/dev/bulk?authorization=JjBE7DtOCur73iBavyaI3cMdFDcRgRcqxHHcctNjqAITOi9iWuyk9acjGMPT&sender_id=FSTSMS&message='.$sms_text.'&language=english&route=qt&numbers='.$phone;
  	
  	//Envoking the API url and getting the response 
  	//$response = file_get_contents($api_url);
  	$ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $api_url);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true );
    // This is what solved the issue (Accepting gzip encoding)
    curl_setopt($ch, CURLOPT_ENCODING, "gzip,deflate");     
    $response = curl_exec($ch);
    curl_close($ch);
    //echo $response;
    //echo $response;
  	//Returning the response 
  	return $response;
}

  //echo "0";
if(isset($_POST['customer_id']) && isset($_POST['restaurant_id']) && isset($_POST['order_status']) && isset($_POST['order_date']) && isset($_POST['order_time'])
          && isset($_POST['order_address']) && isset($_POST['order_payment_mode']) && isset($_POST['total_price']) && isset($_POST['items'])){
  

    //echo "1";

    $uid = mysqli_real_escape_string($conn,htmlspecialchars($_POST['customer_id']));
    //echo $uid;
    $restaurant_id = mysqli_real_escape_string($conn,htmlspecialchars($_POST['restaurant_id']));
    $order_status = mysqli_real_escape_string($conn,htmlspecialchars($_POST['order_status']));
    $order_date = mysqli_real_escape_string($conn,htmlspecialchars($_POST['order_date']));
    $order_time = mysqli_real_escape_string($conn,htmlspecialchars($_POST['order_time']));
    $order_address = mysqli_real_escape_string($conn,htmlspecialchars($_POST['order_address']));
    $order_payment_mode = mysqli_real_escape_string($conn,htmlspecialchars($_POST['order_payment_mode']));
    //$order_lat = mysqli_real_escape_string($conn,htmlspecialchars($_POST['order_lat']));
    //$order_lon = mysqli_real_escape_string($conn,htmlspecialchars($_POST['order_lon']));
    $total_price = mysqli_real_escape_string($conn,htmlspecialchars($_POST['total_price']));
    $items = mysqli_real_escape_string($conn,htmlspecialchars($_POST['items']));
    //echo rtrim($items, "\0");
     $jsonData = stripslashes(html_entity_decode($items));
     //echo $jsonData;
     $unavailable_item = "";
     $proceed = true;
     $json = json_decode($jsonData, true);
  
      foreach ($json as $key => $value) {
          $item_id = $value["foodId"];
          //echo $row['food_id'];
          $query = "SELECT * FROM tbl_food_items WHERE `food_id`='$item_id'";
          $res = mysqli_query($conn,$query);
          $row = mysqli_fetch_assoc($res);
           // echo "2";
            //echo $row['status'];
          if($row['status']!="Available"){
              $unavailable_item = $row['foodName'];
              $proceed = false;
             // echo '1234';
               // echo $unavailable_item;
              break;
          }
          
      }
      
      if($proceed === true){
       
       
            //echo "3";
    
            $uid = mysqli_real_escape_string($conn,htmlspecialchars($_POST['customer_id']));
            $date = date('Y-m-d H:i:s');
            
            $query_upload="INSERT INTO `tbl_order`(`total_price`, `order_date`, `order_time`,`customer_id`, `restaurant_id`, `order_address`,`order_payment_mode`, `order_status`, `created_on`) VALUES ($total_price,'$order_date','$order_time','$uid','$restaurant_id','$order_address','$order_payment_mode','Pending','$date')";
             
              //$query_upload="INSERT INTO `tbl_order`(`total_price`, `order_date`, `customer_id`, `restaurant_id`, `order_address`,`order_payment_mode`,`order_lat`,`order_long`, `order_status`, `created_on`) VALUES ($total_price,'$order_date','$uid','$restaurant_id','$order_address','$order_payment_mode','$order_lat','$order_lon','Pending','$date')";
             if (mysqli_query($conn, $query_upload)) {
    
             
                $last_id = mysqli_insert_id($conn);
               
                $query = "INSERT INTO `tbl_order_Items`(`food_id`, `quantity`,`price`,`order_id`,`created_at`) VALUES ";
                
                if($row['status']!="Available"){
                    $unavailable_item = $row['name'];
                    $proceed = false;
                }      
                
                 $jsonData1 = stripslashes(html_entity_decode($items));
                 
                 $json1 = json_decode($jsonData1, true);  
                
                foreach ($json1 as $key => $value) {
                    $food_id = $value["foodId"];
                    $food_name = $value["foodName"];
                    $quantity = $value["quantity"];
                    $total_price = $value["totalPrice"];
                    $date1 = date('Y-m-d H:i:s');
                    $query = $query."('".$food_id."','".$quantity."',".$total_price.",".$last_id.",'".$date1."'),";
                    
                    // echo $query;
                     
                }
                $query = substr($query,0,strlen($query)-1);
                //echo $query;
                if ($conn->query($query) === TRUE) {

                    $query_restaurant = "SELECT *  FROM `tbl_restaurants` WHERE `restaurant_id`='$restaurant_id'";
                    
                    $result_restaurant = mysqli_query($conn, $query_restaurant);
                    $row_restaurant = mysqli_fetch_array($result_restaurant);
                    
                    $phone = $row_restaurant['phone_number'];
                    sendOtp($last_id,$phone);
                    array_push($response,
                        array('success'=>1,
                              'message'=>"Order placed successfully",
                              'order_id'=>$last_id
                              ));
                } else {
                   //echo "5";
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
         
          }else{
                echo "4";
      
               array_push($response,
                    array('success'=>0,
                          'message'=>$unavailable_item." is currently not available.",
                          'item'=>$unavailable_item
                          ));
              
          
          }
               
}
echo json_encode(array('response' => $response));  