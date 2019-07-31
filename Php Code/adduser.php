<?php
session_start();
include("./config.php");

$response = array();

if(isset($_POST['uid'])){
    
    $uid = mysqli_real_escape_string($conn,htmlspecialchars($_POST['uid']));
    $date = date('Y-m-d H:i:s');
 
    $query_upload="INSERT INTO `tbl_users`(`uid`, `access_role`,`created_at`) VALUES ('$uid','User','$date')";
    
    if (mysqli_query($conn, $query_upload)) {

        $date1 = date('Y-m-d H:i:s');

        $sql_log = "INSERT INTO `tbl_users_log` (`uid`, `login_at`) VALUES ('$uid','$date1')";
        if (mysqli_query($conn,$sql_log)) {
            
            $last_id = mysqli_insert_id($conn);
            //echo $last_id;

            array_push($response,
            array('success'=>1,
                'message'=>"Registration Successful",
                'uid'=> $uid,
                'login_id'=>$last_id
                ));

            }else{
                $log_status="Failed to log data.";                
                $error = $sql_log . "<br>" . $conn->error;
                echo $error;
            }
        
        } else {
            //echo "Error adding user: " . mysqli_error($conn);
            array_push($response,
                array('success'=>0,
                    'message'=>"Registration failed.",
                    'uid'=> $uid
                    ));
        }

}

echo json_encode(array('response' => $response));  

mysqli_close($conn);