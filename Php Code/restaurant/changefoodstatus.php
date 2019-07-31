<?php 
session_start();

//include "../config.php";


if(isset($_POST['food_id']) && isset($_POST['food_status'])){	

require_once('../config.php');
mysqli_set_charset($conn, 'utf8mb4');

		$status = mysqli_real_escape_string($conn,htmlspecialchars($_POST['food_status']));
		$food_id = mysqli_real_escape_string($conn,htmlspecialchars($_POST['food_id']));
    
    if($status == 'Available'){
  
        $response = array();
        $date = date('Y-m-d H:i:s');
        
        $query_upload="UPDATE `tbl_food_items` SET `status`='Available',`modified_at`='$date' WHERE `food_id`='$food_id'";  
        
        if (mysqli_query($conn, $query_upload)) {
        
            array_push($response,
              array('success'=>1,
                  'message'=>"Food status updated successfully.",
                  'food_id'=> $food_id
                  ));
    
        } else {
            //echo "Error adding user: " . mysqli_error($conn);
            array_push($response,
                array('success'=>0,
                    'message'=>"Food status update failed.",
                    'food_id'=> $food_id
                    ));
        }
    
        echo json_encode(array('response' => $response));  
    }else{
        
        $response = array();
        $date = date('Y-m-d H:i:s');
        
        $query_upload="UPDATE `tbl_food_items` SET `status`='Not Available',`modified_at`='$date' WHERE `food_id`='$food_id'";  
        
        if (mysqli_query($conn, $query_upload)) {
        
            array_push($response,
              array('success'=>1,
                  'message'=>"Food status updated successfully.",
                  'food_id'=> $food_id
                  ));
    
        } else {
            //echo "Error adding user: " . mysqli_error($conn);
            array_push($response,
                array('success'=>0,
                    'message'=>"Food status update failed.",
                    'food_id'=> $food_id
                    ));
        }
    
        echo json_encode(array('response' => $response));  


    }
		mysqli_close($conn);
}


