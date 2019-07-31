<?php 
session_start();

//include "../config.php";


if(isset($_POST['restaurant_id']) && isset($_POST['restaurant_status'])){	

require_once('../config.php');
mysqli_set_charset($conn, 'utf8mb4');

		$status = mysqli_real_escape_string($conn,htmlspecialchars($_POST['restaurant_status']));
		$restaurant_id = mysqli_real_escape_string($conn,htmlspecialchars($_POST['restaurant_id']));
    
    if($status == 'Open'){
  
        $response = array();
        $date = date('Y-m-d H:i:s');
        
        $query_upload="UPDATE `tbl_restaurants` SET `status_tag`='Open',`modified_at`='$date' WHERE `restaurant_id`='$restaurant_id'";  
        
        if (mysqli_query($conn, $query_upload)) {
        
            array_push($response,
              array('success'=>1,
                  'message'=>"Restaurant status updated successfully.",
                  'restaurant_id'=> $restaurant_id
                  ));
    
        } else {
            //echo "Error adding user: " . mysqli_error($conn);
            array_push($response,
                array('success'=>0,
                    'message'=>"Restaurant status update failed.",
                    'restaurant_id'=> $restaurant_id
                    ));
        }
    
        echo json_encode(array('response' => $response));  
    }else{
        
        $response = array();
        $date = date('Y-m-d H:i:s');
        
        $query_upload="UPDATE `tbl_restaurants` SET `status_tag`='Closed',`modified_at`='$date' WHERE `restaurant_id`='$restaurant_id'";  
        
        if (mysqli_query($conn, $query_upload)) {
        
            array_push($response,
              array('success'=>1,
                  'message'=>"Restaurant status updated successfully.",
                  'restaurant_id'=> $restaurant_id
                  ));
    
        } else {
            //echo "Error adding user: " . mysqli_error($conn);
            array_push($response,
                array('success'=>0,
                    'message'=>"Restaurant status update failed.",
                    'restaurant_id'=> $restaurant_id
                    ));
        }
    
        echo json_encode(array('response' => $response));  


    }
		mysqli_close($conn);
}


