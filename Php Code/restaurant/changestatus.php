<?php 
session_start();

//include "../config.php";

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


if(isset($_POST['order_id']) && isset($_POST['order_status']) && isset($_POST['order_feedback']) ){	

require_once('../config.php');
mysqli_set_charset($conn, 'utf8mb4');

		$status = mysqli_real_escape_string($conn,htmlspecialchars($_POST['order_status']));
		$order_id = mysqli_real_escape_string($conn,htmlspecialchars($_POST['order_id']));
    $order_feedback = mysqli_real_escape_string($conn,htmlspecialchars($_POST['order_feedback']));
    
    if($status == 'Rejected_Restaurant'){
        
        $response = array();
        $date = date('Y-m-d H:i:s');
        
        $query_upload="UPDATE `tbl_order` SET `order_status`='Rejected',`order_feedback`='$order_feedback',`modified_at`='$date' WHERE `order_id`=$order_id";  
        
        if (mysqli_query($conn, $query_upload)) {
        
            array_push($response,
              array('success'=>1,
                  'message'=>"Order status updated successfully.",
                  'order_id'=> $order_id
                  ));
    
        } else {
            //echo "Error adding user: " . mysqli_error($conn);
            array_push($response,
                array('success'=>0,
                    'message'=>"Order status update failed.",
                    'order_id'=> $order_id
                    ));
        }
    
        echo json_encode(array('response' => $response));  


    }else{
        $response = array();
        //$query_upload = "";
        $date = date('Y-m-d H:i:s');
          
        if($status=='Accepted_Restaurant'){
            
            $query_upload="UPDATE `tbl_order` SET `order_status`='Accepted_Restaurant',`modified_at`='$date' WHERE `order_id`=$order_id";
                
        }else if($status=='Ready_Restaurant'){
        
            $query_upload="UPDATE `tbl_order` SET `order_status`='Ready_Restaurant',`modified_at`='$date' WHERE `order_id`=$order_id";
                
        }else if($status=='Accepted_Delivery'){
    
            $query_upload="UPDATE `tbl_order` SET `order_status`='Accepted_Delivery',`modified_at`='$date' WHERE `order_id`=$order_id";
                
        }else if($status=='Rejected_Delivery'){
    
            $query_upload="UPDATE `tbl_order` SET `order_status`='Rejected_Delivery',`modified_at`='$date' WHERE `order_id`=$order_id";
                
        }else if($status=='Collected_Delivery'){
    
            $query_upload="UPDATE `tbl_order` SET `order_status`='Collected_Delivery',`modified_at`='$date' WHERE `order_id`=$order_id";
                
        }else if($status=='Delivered'){
        
            $query_upload="UPDATE `tbl_order` SET `order_status`='Completed',`order_delivery_time`='$date',`modified_at`='$date' WHERE `order_id`=$order_id";
                
        }
    
    //echo $query_upload;   
        if (mysqli_query($conn, $query_upload)) {

            $last_id = mysqli_insert_id($conn);
            
            if($status=='Accepted_Restaurant'){

                $query_od = "SELECT *  FROM `tbl_order` WHERE `order_id`=$order_id";
        
                $result_od = mysqli_query($conn, $query_od);
                $row_od = mysqli_fetch_array($result_od);

                $query_restau = "SELECT *  FROM `tbl_restaurants` WHERE `restaurant_id`='$row_od[`restaurant_id`]'";
        
                $result_restau = mysqli_query($conn, $query_restau);
                $row_restau = mysqli_fetch_array($result_restau);
                
                $lat = $row_restau['location_lat'];
                $lon = $row_restau['location_long'];

                $sql="SELECT *, ( 6371 * acos( cos( radians($lat) ) * cos( radians( `current_lat` ) ) * cos( radians( `current_long` ) - radians($lon) ) + sin( radians($lat) ) * sin( radians(`current_lat` ) ) ) ) AS distance FROM `tbl_delivery` HAVING distance < 10 order by distance DESC LIMIT 1";
        
        
                if($result1 = mysqli_query($conn, $sql)){
                
                    $row = mysqli_fetch_array($result1);
                    
                    $query_delivery = "SELECT *  FROM `tbl_delivery` WHERE `id`=$row[`delivery_id`]";
            
                    $result_delivery = mysqli_query($conn, $query_delivery);
                    $row_delivery = mysqli_fetch_array($result_delivery);
                    
                    $phone = $row_delivery['phone_number'];
                    $d_id=$row_delivery['delivery_id'];
                    $query_upload1="UPDATE `tbl_order` SET `delivery_id`='$d_id',`modified_at`='$date' WHERE `order_id`=$order_id";
                    
                    if (mysqli_query($conn, $query_upload)) {
                    
                        sendOtp($last_id,$phone);
            
                        array_push($response,
                          array('success'=>1,
                              'message'=>"Order status updated successfully.",
                              'order_id'=> $order_id
                              ));                    
                    }else{
                        array_push($response,
                            array('success'=>0,
                                'message'=>"Server issue please try again later.",
                                ));
                                
                    }
                }else{
                    //echo "Error adding user: " . mysqli_error($conn);
                    array_push($response,
                      array('success'=>1,
                          'message'=>"There are not delivery executives.",
                          'order_id'=> $order_id
                          ));

                }

            }else{
      
                 array_push($response,
                    array('success'=>1,
                        'message'=>"Order status updated successfully.",
                        'order_id'=> $order_id
                        ));
       
            }
        
        } else {
            //echo "Error adding user: " . mysqli_error($conn);
            array_push($response,
                array('success'=>0,
                    'message'=>"Order status update failed.",
                    'order_id'=> $order_id
                    ));
        }
        
      echo json_encode(array('response' => $response));  

    }
		mysqli_close($conn);
}
