<?php
session_start();

//include "../config.php";


if(isset($_POST['delivery_id']) && isset($_POST['location_lat']) && isset($_POST['location_long'])){	

    require_once('../config.php');
    mysqli_set_charset($conn, 'utf8mb4');

		$location_lat = mysqli_real_escape_string($conn,htmlspecialchars($_POST['location_lat']));
		$location_long = mysqli_real_escape_string($conn,htmlspecialchars($_POST['location_long']));
		$delivery_id = mysqli_real_escape_string($conn,htmlspecialchars($_POST['delivery_id']));
    
    $response = array();
    $date = date('Y-m-d H:i:s');
    
    $query_upload="UPDATE `tbl_delivery` SET `current_lat`='$location_lat', `current_long`='$location_long', `last_modified_at`='$date' WHERE `delivery_id`=$delivery_id";  
    
    if (mysqli_query($conn, $query_upload)) {
    
        array_push($response,
          array('success'=>1,
              'message'=>"location updated successfully.",
              'delivery_id'=> $delivery_id
              ));

    } else {
        //echo "Error adding user: " . mysqli_error($conn);
        array_push($response,
            array('success'=>0,
                'message'=>"location update failed.",
                'delivery_id'=> $delivery_id
                ));
    }

    echo json_encode(array('response' => $response));  
		mysqli_close($conn);
}
