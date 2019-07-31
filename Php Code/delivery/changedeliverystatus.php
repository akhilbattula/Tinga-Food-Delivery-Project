<?php
session_start();

//include "../config.php";


if(isset($_POST['delivery_id']) && isset($_POST['delivery_status'])){	

    require_once('../config.php');
    mysqli_set_charset($conn, 'utf8mb4');

		$status = mysqli_real_escape_string($conn,htmlspecialchars($_POST['delivery_status']));
		$delivery_id = mysqli_real_escape_string($conn,htmlspecialchars($_POST['delivery_id']));
    
    if($status == 'Available'){
  
        $response = array();
        $date = date('Y-m-d H:i:s');
        
        $query_upload="UPDATE `tbl_delivery` SET `status`='Available',`last_modified_at`='$date' WHERE `delivery_id`=$delivery_id";  
        
        if (mysqli_query($conn, $query_upload)) {
        
            array_push($response,
              array('success'=>1,
                  'message'=>"Status updated successfully.",
                  'delivery_id'=> $delivery_id
                  ));
    
        } else {
            //echo "Error adding user: " . mysqli_error($conn);
            array_push($response,
                array('success'=>0,
                    'message'=>"Status update failed.",
                    'delivery_id'=> $delivery_id
                    ));
        }
    
        echo json_encode(array('response' => $response));  
    }else{
        
        $response = array();
        $date = date('Y-m-d H:i:s');
        
        $query_upload="UPDATE `tbl_delivery` SET `status`='Unavailable',`last_modified_at`='$date' WHERE `delivery_id`=$delivery_id";  
        
        if (mysqli_query($conn, $query_upload)) {
        
            array_push($response,
              array('success'=>1,
                  'message'=>"Status updated successfully.",
                  'delivery_id'=> $delivery_id
                  ));
    
        } else {
            //echo "Error adding user: " . mysqli_error($conn);
            array_push($response,
                array('success'=>0,
                    'message'=>"Status update failed.",
                    'delivery_id'=> $delivery_id
                    ));
        }
    
        echo json_encode(array('response' => $response));  
    }
		mysqli_close($conn);
}
