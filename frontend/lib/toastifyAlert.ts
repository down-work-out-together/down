import { Flip, toast } from "react-toastify";

export const ToastSuccess = (alertText: string) => {
	toast.success(`${alertText}`, {
		position: "top-right",
		autoClose: 2000,
	});
	return;
};

export const ToastWarn = (alertText: string) => {
	toast.warn(`${alertText}`, {
		position: "top-right",
		autoClose: 2000,
	});
	return;
};

export const ToastError = (alertText: string) => {
	toast.error(`${alertText}`, {
		position: "top-right",
		autoClose: 2000,
	});
	return;
};

export const ToastInfo = (alertText: string) => {
	toast.info(`${alertText}`, {
		position: "top-right",
		autoClose: 2000,
	});
};

export const toastAlert = (alertText: string) => {
	toast(`${alertText}`, {
		position: "top-right",
		autoClose: 2000,
		hideProgressBar: true,
		closeOnClick: true,
		pauseOnHover: true,
		draggable: true,
		progress: undefined,
		theme: "light",
		transition: Flip,
	});
};
