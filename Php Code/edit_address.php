<?php
session_start();
include("./config.php");

$response = array();

if(isset($_POST['id']) && isset($_POST['title']) && isset($_POST['address']) && isset($_POST['latitude']) && isset($_POST['longitude'])){
    
    $id = mysqli_real_escape_string($conn,htmlspecialchars($_POST['id']));
    $title = mysqli_real_escape_string($conn,htmlspecialchars($_POST['title']));
    $address = mysqli_real_escape_string($conn,htmlspecialchars($_POST['address']));
    $latitude = mysqli_real_escape_string($conn,htmlspecialchars($_POST['latitude']));
    $longitude = mysqli_real_escape_string($conn,htmlspecialchars($_POST['longitude']));
    $date = date('Y-m-d H:i:s');
 
    $query_upload="UPDATE `tbl_address` SET `title`='$title',`address`='$address',`location_lat`='$latitude',`location_long`='$longitude' WHERE `id`=$id";
    
    if (mysqli_query($conn, $query_upload)) {

            array('success'=>1,
                'message'=>"Address added successfully",
                'uid'=> $uid
                ));

            }else{
                $log_status="Unable to address.";                
                $error = $sql_log . "<br>" . $conn->error;
                echo $error;
            }f

}

echo json_encode(array('response' => $response));  

mysqli_close($conn);