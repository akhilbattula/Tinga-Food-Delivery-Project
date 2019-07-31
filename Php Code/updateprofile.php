<?php
session_start();
include("./config.php");

$response = array();
//echo "CHeck user";

// echo $_POST['fname'];

if(isset($_POST['fname']) && isset($_POST['lname']) && isset($_POST['uid']) && isset($_POST['phone_number']) && isset($_POST['mobile_verified']) && isset($_POST['email'])){
     
    $fname = mysqli_real_escape_string($conn,htmlspecialchars($_POST['fname']));
    $lname = mysqli_real_escape_string($conn,htmlspecialchars($_POST['lname']));
    $uid = mysqli_real_escape_string($conn,htmlspecialchars($_POST['uid']));
    
    $phone_number = mysqli_real_escape_string($conn,htmlspecialchars($_POST['phone_number']));
    $mobile_verified = mysqli_real_escape_string($conn,htmlspecialchars($_POST['mobile_verified']));
    $email = mysqli_real_escape_string($conn,htmlspecialchars($_POST['email']));
    $date = date('Y-m-d H:i:s');

    $query_upload="UPDATE `tbl_users` SET `fname`='$fname',`lname`='$lname',`phone_number`='$phone_number',`mobile_verified`='$mobile_verified',`email`='$email',`last_modified_at`='$date' WHERE `uid`='$uid'";
    
    if (mysqli_query($conn, $query_upload)) {

        array_push($response,
            array('success'=>1,
                'message'=>"Profile Update Successful.",
                'uid'=> $uid
                ));
        
        } else {
            //echo "Error updating record: " . mysqli_error($conn);
            array_push($response,
                array('success'=>0,
                    'message'=>"Failed to update profile.",
                    'uid'=> $uid,
                    "Error"=>"Error updating record: " . mysqli_error($conn)
                    ));
        }

}

echo json_encode(array('response' => $response));  

//mysqli_close($conn);  