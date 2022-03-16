import React from "react";
import Snackbar from "@mui/material/Snackbar";
import MuiAlert from "@mui/material/Alert";

// Alert Function
const Alert = React.forwardRef(function Alert(props, ref) {
  return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

const SnackbarPopup = (props) => {
  // SNACKBAR ALERT CLOSE
  const handleClose = (event, reason) => {
    if (reason === "clickaway") {
      return;
    }

    // Set NOTIFICATION CLOSE
    props.setNotification({
      isOpen: false,
    });
  };

  return (
    <Snackbar
      anchorOrigin={{
        vertical: "bottom",
        horizontal: "left",
      }}
      open={props.notification.isOpen}
      autoHideDuration={4000}
      onClose={handleClose}
    >
      <Alert onClose={handleClose} severity={props.notification.isSuccess ? "success" : "error"}>
        {props.notification.message}
      </Alert>
    </Snackbar>
  );
};

export default SnackbarPopup;
