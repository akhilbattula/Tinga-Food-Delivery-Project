<?php
session_start();
include("./config.php");

$response = array();

if(isset($_POST['uid']) && isset($_POST['title']) && isset($_POST['address']) && isset($_POST['location_lat']) && isset($_POST['location_long'])){
    
    $uid = mysqli_real_escape_string($conn,htmlspecialchars($_POST['uid']));
    $title = mysqli_real_escape_string($conn,htmlspecialchars($_POST['title']));
    $address = mysqli_real_escape_string($conn,htmlspecialchars($_POST['address']));
    $latitude = mysqli_real_escape_string($conn,htmlspecialchars($_POST['location_lat']));
    $longitude = mysqli_real_escape_string($conn,htmlspecialchars($_POST['location_long']));
    $date = date('Y-m-d H:i:s');
 
    $query_upload="INSERT INTO `tbl_address`(`uid`,`title`,`address`,`location_lat`,`location_long`,`created_at`) VALUES ('$uid','$title','$address','$latitude','$longitude','$date')";
        
    if (mysqli_query($conn, $query_upload)) {

            $last_id = mysqli_insert_id($conn);
            array_push($response,
            array('success'=>1,
                'message'=>"Registration Successful",
                'uid'=> $uid,
                'id'=>$last_id
                ));

    }else{
        $log_status="Unable to address.";                
        $error = $sql_log . "<br>" . $conn->error;
        echo $error;
    }

}

echo json_encode(array('response' => $response));  

mysqli_close($conn);