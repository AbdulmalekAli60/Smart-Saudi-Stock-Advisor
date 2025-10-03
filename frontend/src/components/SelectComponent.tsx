import Select from "react-select";
import { SelectedValue } from "../Interfaces/SelectedValueInterface";

interface SelectComponentProps {
  value: { value: string; label: string };
  options: {value: string;label: string;}[]| undefined;
  setValeu: React.Dispatch<React.SetStateAction<SelectedValue>>;
  field: "from" | "to";
  placeholder: string;
}

export default function SelectComponent({
  options,
  setValeu,
  value,
  field,
  placeholder
}: SelectComponentProps) {
  return (
    <Select
      isSearchable={true}
      isClearable={true}
      value={value?.value ? value : null}
      placeholder={placeholder}
      options={options}
      onChange={(option) => {
        setValeu((prev) => ({
          ...prev,
          [field]: option || { value: "", label: "" },
        }));
      }}
      styles={{
        control: (base, state) => ({
          ...base,
          minWidth: "180px",
          borderRadius: "12px",
          border: state.isFocused ? "2px solid #3b82f6" : "1px solid #e5e7eb",
          boxShadow: "none",
          padding: "2px 4px",
          cursor: "pointer",
          transition: "all 0.2s",
          "&:hover": {
            borderColor: "#3b82f6",
          },
        }),
        menu: (base) => ({
          ...base,
          borderRadius: "12px",
          overflow: "hidden",
          boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
          border: "1px solid #e5e7eb",
        }),
        menuList: (base) => ({
          ...base,
          padding: "4px",
        }),
        option: (base, state) => ({
          ...base,
          backgroundColor: state.isSelected
            ? "#3b82f6"
            : state.isFocused
            ? "#eff6ff"
            : "white",
          color: state.isSelected ? "white" : "#374151",
          cursor: "pointer",
          borderRadius: "8px",
          padding: "8px 12px",
          fontSize: "14px",
          transition: "all 0.15s",
        }),
        placeholder: (base) => ({
          ...base,
          color: "#9ca3af",
          fontSize: "14px",
        }),
        singleValue: (base) => ({
          ...base,
          color: "#1f2937",
          fontSize: "14px",
          fontWeight: "500",
        }),
      }}
    />
  );
}
